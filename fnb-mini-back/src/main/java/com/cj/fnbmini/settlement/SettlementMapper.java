package com.cj.fnbmini.settlement;

import com.cj.fnbmini.settlement.dto.SettlementSearchDto;
import com.cj.fnbmini.settlement.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface SettlementMapper {

    // ===== 매장 =====
    List<Shop> selectShopList();

    // ===== 일매출 =====
    List<DailySales> selectDailySalesList(SettlementSearchDto searchDto);

    /** 비관적 락 - 정산 대상 행을 잠금 (SELECT FOR UPDATE) */
    DailySales selectDailySalesForUpdate(@Param("shopId") Long shopId,
                                         @Param("salesDate") LocalDate salesDate);

    // ===== 정산 =====
    List<Settlement> selectSettlementList(SettlementSearchDto searchDto);

    Settlement selectSettlementById(Long id);

    /** 멱등성 체크 - 동일 키로 이미 처리된 건 조회 */
    Settlement selectByIdempotencyKey(String idempotencyKey);

    int insertSettlement(Settlement settlement);

    int updateSettlementStatus(@Param("id") Long id,
                               @Param("status") String status,
                               @Param("sapSyncStatus") String sapSyncStatus,
                               @Param("sapSyncError") String sapSyncError);

    /** 낙관적 락 - 정산 상태 변경 시 충돌 감지 */
    int updateSettlementWithVersion(Settlement settlement);

    // ===== 원장 =====

    /** 기존 원장 역분개 처리 (del_yn='Y') */
    int markLedgerAsDeleted(@Param("settlementId") Long settlementId);

    int insertSettlementLedger(SettlementLedger ledger);

    // ===== 이력 =====
    int insertSettlementSyncHistory(SettlementSyncHistory history);

    /** 실패 정산 조회 (재시도 대상) */
    List<Settlement> selectFailedSettlements();
}
