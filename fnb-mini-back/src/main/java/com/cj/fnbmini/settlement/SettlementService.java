package com.cj.fnbmini.settlement;

import com.cj.fnbmini.common.exception.BrandException;
import com.cj.fnbmini.settlement.dto.SettlementReqDto;
import com.cj.fnbmini.settlement.dto.SettlementSearchDto;
import com.cj.fnbmini.settlement.dto.SettlementSyncResultDto;
import com.cj.fnbmini.settlement.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 정산 비즈니스 로직
 *
 * ============================================================
 * 브랜드 관리와 동일한 조치:
 *   1. 트랜잭션 3분할 (커넥션 풀 고갈 방지)
 *   2. Self-Proxy (@Lazy @Autowired self)
 *   3. 이력 테이블 (동기화 결과 기록)
 *   4. 상태 추적 (READY → PROCESSING → SETTLED / FAILED)
 *
 * 정산에서 추가된 조치:
 *   5. 비관적 락 (SELECT FOR UPDATE) — 동시 정산 방지
 *   6. 멱등성 키 (idempotency_key) — 중복 정산 방지
 *   7. 보상 트랜잭션 — 부분 성공 시 취소
 * ============================================================
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementMapper settlementMapper;
    private final SettlementSyncService settlementSyncService;

    /** self-proxy: 같은 클래스 내 @Transactional 호출을 위해 */
    @Lazy
    @Autowired
    private SettlementService self;

    // ===== 조회 =====

    public List<Shop> getShopList() {
        return settlementMapper.selectShopList();
    }

    public List<DailySales> getDailySalesList(SettlementSearchDto searchDto) {
        return settlementMapper.selectDailySalesList(searchDto);
    }

    public List<Settlement> getSettlementList(SettlementSearchDto searchDto) {
        return settlementMapper.selectSettlementList(searchDto);
    }

    // ===== 정산 실행 (오케스트레이터 — TX 없음) =====

    /**
     * 정산 오케스트레이터
     * 주의: 이 메서드에는 @Transactional이 없다!
     */
    public Settlement settle(SettlementReqDto req) {
        Long shopId = req.getShopId();
        LocalDate salesDate = LocalDate.parse(req.getSalesDate());
        String idempotencyKey = req.getIdempotencyKey();

        log.info("========== 정산 시작 (shopId={}, date={}) ==========", shopId, salesDate);

        // ★ 조치 6: 멱등성 키 체크 (중복 요청 방지)
        Settlement existing = settlementMapper.selectByIdempotencyKey(idempotencyKey);
        if (existing != null) {
            log.info("[멱등성] 이미 처리된 요청 (key={}, status={})", idempotencyKey, existing.getStatus());
            return existing;
        }

        // 1차: DB 정산 처리 (비관적 락 + 원장 생성)
        log.info("[1차] 정산 데이터 생성 시작 (비관적 락 적용)");
        Settlement settlement = self.createSettlement(shopId, salesDate, idempotencyKey);
        log.info("[1차] 정산 데이터 생성 완료 - 커넥션 반환됨 (settlementId={})", settlement.getId());

        // 2차: 외부 API(SAP) 동기화 (TX 없음 → 커넥션 점유 없음)
        log.info("[2차] 외부 API(SAP) 동기화 시작 - 커넥션 점유 없음");
        List<SettlementSyncResultDto> syncResults =
                settlementSyncService.syncToExternalApi(settlement);
        log.info("[2차] 외부 API(SAP) 동기화 완료");

        // ★ 조치 7: 보상 트랜잭션 (부분 성공 시 취소)
        boolean hasReverseFail = syncResults.stream()
                .anyMatch(r -> "REVERSE".equals(r.getSyncType()) && !r.isSuccess());
        boolean hasNormalFail = syncResults.stream()
                .anyMatch(r -> "SETTLE".equals(r.getSyncType()) && !r.isSuccess());
        boolean reverseSuccess = syncResults.stream()
                .anyMatch(r -> "REVERSE".equals(r.getSyncType()) && r.isSuccess());

        if (reverseSuccess && hasNormalFail) {
            // 역분개 성공 + 신규 전표 실패 → 역분개 취소 (보상)
            log.warn("[보상] 역분개 성공 + 신규 전표 실패 → 역분개 취소 요청");
            SettlementSyncResultDto cancelResult =
                    settlementSyncService.cancelReverse(settlement);
            syncResults.add(cancelResult);
        }

        // 3차: 결과 반영 + 이력 저장
        boolean allSuccess = syncResults.stream().allMatch(SettlementSyncResultDto::isSuccess)
                && !hasNormalFail;
        log.info("[3차] 동기화 결과 반영 시작 (success={})", allSuccess);
        self.updateSyncResults(settlement.getId(), syncResults, allSuccess);
        log.info("[3차] 동기화 결과 반영 완료 - 커넥션 반환됨");

        log.info("========== 정산 완료 (status={}) ==========", allSuccess ? "SETTLED" : "FAILED");

        return settlementMapper.selectSettlementById(settlement.getId());
    }

    /**
     * TX#1: 비관적 락 + 정산 데이터 생성 + 원장 생성
     * 커밋 후 커넥션이 풀로 반환된다.
     */
    @Transactional
    public Settlement createSettlement(Long shopId, LocalDate salesDate, String idempotencyKey) {

        // ★ 조치 5: 비관적 락 (SELECT FOR UPDATE)
        // → 같은 매장/날짜에 대해 다른 TX가 동시 정산 시도하면 여기서 대기
        DailySales dailySales = settlementMapper.selectDailySalesForUpdate(shopId, salesDate);
        if (dailySales == null) {
            throw new BrandException("일매출 데이터가 없습니다: shopId=" + shopId + ", date=" + salesDate);
        }

        // 이미 정산된 건 확인
        SettlementSearchDto check = new SettlementSearchDto();
        check.setShopId(shopId);
        check.setSalesDateFrom(salesDate.toString());
        check.setSalesDateTo(salesDate.toString());
        List<Settlement> existingList = settlementMapper.selectSettlementList(check);

        Settlement settlement = new Settlement();
        settlement.setShopId(shopId);
        settlement.setSalesDate(salesDate);
        settlement.setTotalAmt(dailySales.getTotalAmt());
        settlement.setIdempotencyKey(idempotencyKey);
        settlement.setSettledBy("admin"); // TODO: JWT에서 추출

        if (!existingList.isEmpty()) {
            // 재정산: 기존 원장 역분개 처리
            Settlement prev = existingList.getFirst();
            log.info("재정산 감지 - 기존 원장 역분개 (prevId={})", prev.getId());
            settlementMapper.markLedgerAsDeleted(prev.getId());

            // 역분개 원장 INSERT
            SettlementLedger reverseLedger = new SettlementLedger();
            reverseLedger.setSettlementId(prev.getId());
            reverseLedger.setShopId(shopId);
            reverseLedger.setSalesDate(salesDate);
            reverseLedger.setLedgerType("REVERSE");
            reverseLedger.setCashAmt(-dailySales.getCashAmt());
            reverseLedger.setCardAmt(-dailySales.getCardAmt());
            reverseLedger.setEasyAmt(-dailySales.getEasyAmt());
            reverseLedger.setTotalAmt(-dailySales.getTotalAmt());
            settlementMapper.insertSettlementLedger(reverseLedger);

            // 기존 정산 상태 업데이트
            settlementMapper.updateSettlementStatus(prev.getId(), "REPLACED", "NONE", null);
        }

        // 신규 정산 INSERT
        settlement.setStatus("PROCESSING");
        settlementMapper.insertSettlement(settlement);

        // 신규 원장 INSERT (NORMAL)
        SettlementLedger normalLedger = new SettlementLedger();
        normalLedger.setSettlementId(settlement.getId());
        normalLedger.setShopId(shopId);
        normalLedger.setSalesDate(salesDate);
        normalLedger.setLedgerType("NORMAL");
        normalLedger.setCashAmt(dailySales.getCashAmt());
        normalLedger.setCardAmt(dailySales.getCardAmt());
        normalLedger.setEasyAmt(dailySales.getEasyAmt());
        normalLedger.setTotalAmt(dailySales.getTotalAmt());
        settlementMapper.insertSettlementLedger(normalLedger);

        return settlement;
    }

    /**
     * TX#2: 동기화 결과 반영 + 이력 저장
     */
    @Transactional
    public void updateSyncResults(Long settlementId, List<SettlementSyncResultDto> results, boolean allSuccess) {
        String status = allSuccess ? "SETTLED" : "FAILED";
        String sapStatus = allSuccess ? "SUCCESS" : "FAILED";
        String error = results.stream()
                .filter(r -> !r.isSuccess())
                .map(SettlementSyncResultDto::getErrorMessage)
                .findFirst().orElse(null);

        settlementMapper.updateSettlementStatus(settlementId, status, sapStatus, error);

        for (SettlementSyncResultDto result : results) {
            SettlementSyncHistory history = new SettlementSyncHistory();
            history.setSettlementId(settlementId);
            history.setSyncType(result.getSyncType());
            history.setSyncStatus(result.isSuccess() ? "SUCCESS" : "FAILED");
            history.setRequestPayload(result.getRequestPayload());
            history.setResponsePayload(result.getResponsePayload());
            history.setErrorMessage(result.getErrorMessage());
            settlementMapper.insertSettlementSyncHistory(history);
        }
    }

    // ===== 재시도 =====

    public Settlement retrySettlement(Long id) {
        Settlement settlement = settlementMapper.selectSettlementById(id);
        if (settlement == null) {
            throw new BrandException("정산 데이터를 찾을 수 없습니다: " + id);
        }
        if (!"FAILED".equals(settlement.getSapSyncStatus())) {
            throw new BrandException("FAILED 상태의 정산만 재시도할 수 있습니다.");
        }

        log.info("정산 재시도 시작 (id={})", id);
        List<SettlementSyncResultDto> syncResults =
                settlementSyncService.syncToExternalApi(settlement);

        boolean allSuccess = syncResults.stream().allMatch(SettlementSyncResultDto::isSuccess);
        self.updateSyncResults(id, syncResults, allSuccess);

        return settlementMapper.selectSettlementById(id);
    }
}
