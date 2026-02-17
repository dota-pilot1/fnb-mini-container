package com.cj.fnbmini.brand;

import com.cj.fnbmini.brand.dto.BrandSaveReqDto;
import com.cj.fnbmini.brand.dto.BrandSearchDto;
import com.cj.fnbmini.brand.dto.BrandSyncResultDto;
import com.cj.fnbmini.brand.entity.Brand;
import com.cj.fnbmini.brand.entity.BrandSyncHistory;
import com.cj.fnbmini.common.exception.BrandException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 브랜드 비즈니스 로직
 *
 * ============================================================
 * 핵심 개선: 트랜잭션 3분할
 * ============================================================
 *
 * [실무 문제 구조]
 * @Transactional
 * savePartenrBrands() {
 *     INSERT/UPDATE brand      ← ~50ms  (커넥션 필요)
 *     외부 API CP204 호출      ← ~3초   (커넥션 불필요인데 점유 중!)
 *     외부 API CP206 호출      ← ~3초   (커넥션 불필요인데 점유 중!)
 * }  // 총 ~6초 커넥션 점유 → HikariCP 10개면 동시 10요청에 전체 마비
 *
 * [개선 구조]
 * saveBrands() {                              ← 오케스트레이터 (TX 없음)
 *     TX#1: self.saveBrandsToDb()             ← ~50ms → 커밋 → 커넥션 반환
 *     NO TX: syncService.syncToExternalApi()  ← ~6초 (커넥션 점유 없음!)
 *     TX#2: self.updateSyncResults()          ← ~30ms → 커밋 → 커넥션 반환
 * }  // 총 커넥션 점유 ~80ms → 75배 개선
 * ============================================================
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandMapper brandMapper;
    private final BrandSyncService brandSyncService;

    /**
     * self 주입 패턴
     *
     * @Transactional은 Spring AOP 프록시를 통해 동작한다.
     * 같은 클래스 내에서 this.saveBrandsToDb()를 호출하면
     * 프록시를 거치지 않아 @Transactional이 무시된다.
     *
     * 해결: 자기 자신의 프록시를 주입받아 self.saveBrandsToDb()로 호출
     * → AOP 프록시를 거치므로 @Transactional이 정상 동작
     *
     * @Lazy: Spring Boot 3.x에서는 순환참조가 기본 금지.
     * @Lazy를 붙이면 실제 사용 시점까지 프록시 생성을 지연시켜
     * 빈 생성 시점의 순환참조를 회피한다.
     */
    @Lazy
    @Autowired
    private BrandService self;

    // ===== 조회 =====

    public List<Brand> getBrandList(BrandSearchDto searchDto) {
        return brandMapper.selectBrandList(searchDto);
    }

    public Brand getBrandById(Long id) {
        Brand brand = brandMapper.selectBrandById(id);
        if (brand == null) {
            throw new BrandException("브랜드를 찾을 수 없습니다: " + id);
        }
        return brand;
    }

    public List<Brand> getFailedSyncBrands() {
        return brandMapper.selectFailedSyncBrands();
    }

    // ===== 저장 (오케스트레이터 - 트랜잭션 3분할) =====

    /**
     * 브랜드 저장 오케스트레이터
     * 주의: 이 메서드에는 @Transactional이 없다!
     */
    public void saveBrands(List<BrandSaveReqDto> list) {
        log.info("========== 브랜드 저장 시작 ({}건) ==========", list.size());

        // 1차 : 메인 서버 디비에 브랜드 정보 저장
        log.info("[1차] 메인 서버 DB 저장 시작");
        List<Brand> savedBrands = self.saveBrandsToDb(list);
        log.info("[1차] 메인 서버 DB 저장 완료 - 커넥션 반환됨 ({}건 동기화 대상)", savedBrands.size());

        if (savedBrands.isEmpty()) {
            log.info("동기화 대상 없음 - 완료");
            return;
        }

        // 2차 : 외부 API 동기화 (TX 없음 → 커넥션 점유 없음)
        log.info("[2차] 외부 API 동기화 시작 - 커넥션 점유 없음");
        List<BrandSyncResultDto> syncResults = brandSyncService.syncToExternalApi(savedBrands);
        log.info("[2차] 외부 API 동기화 완료");

        // 3차 : 동기화 이력 테이블에 결과 반영 (2차 API 호출 결과를 DB에 반영)
        log.info("[3차] 동기화 이력 테이블에 결과 반영 시작");
        self.updateSyncResults(syncResults);
        log.info("[3차] 동기화 이력 테이블에 결과 반영 완료 - 커넥션 반환됨");

        log.info("========== 브랜드 저장 완료 ==========");
    }

    /**
     * TX#1: DB 저장 + sync_status를 PENDING으로 설정
     * 커밋 후 커넥션이 풀로 반환된다.
     */
    @Transactional
    public List<Brand> saveBrandsToDb(List<BrandSaveReqDto> list) {
        List<Brand> saved = new ArrayList<>();

        for (BrandSaveReqDto req : list) {
            Brand brand = toBrandEntity(req);

            switch (req.getStatus()) {
                case "C" -> {
                    brandMapper.insertBrand(brand);     // sync_status='PENDING' (SQL에서)
                    brand.setSyncType("REGISTER");
                    saved.add(brand);                   // useGeneratedKeys로 id 세팅됨
                    log.debug("INSERT brand: {} (id={})", brand.getBrandCode(), brand.getId());
                }
                case "U" -> {
                    // 낙관적 락 적용 (Phase 4)
                    int updated = brandMapper.updateBrandWithVersion(brand);
                    if (updated == 0) {
                        throw new BrandException(
                                "브랜드 '" + brand.getBrandCode() + "': " +
                                "다른 사용자가 먼저 수정했습니다. 새로고침 후 다시 시도하세요.");
                    }
                    brand.setSyncType("UPDATE");
                    saved.add(brand);
                    log.debug("UPDATE brand: {} (id={})", brand.getBrandCode(), brand.getId());
                }
                default -> log.warn("Unknown status: {}", req.getStatus());
            }
        }

        return saved;
    }

    /**
     * TX#2: 동기화 결과 반영 + 이력 저장
     * 새 커넥션을 획득하여 결과를 반영하고, 커밋 후 반환한다.
     */
    @Transactional
    public void updateSyncResults(List<BrandSyncResultDto> results) {
        for (BrandSyncResultDto result : results) {
            String status = result.isSuccess() ? "SUCCESS" : "FAILED";
            String error = result.isSuccess() ? null : result.getErrorMessage();

            // brand 테이블 sync_status 업데이트
            brandMapper.updateSyncStatus(result.getBrandId(), status, error);

            // brand_sync_history 이력 저장
            BrandSyncHistory history = new BrandSyncHistory();
            history.setBrandId(result.getBrandId());
            history.setSyncType(result.getSyncType());
            history.setSyncStatus(status);
            history.setRequestPayload(result.getRequestPayload());
            history.setResponsePayload(result.getResponsePayload());
            history.setErrorMessage(error);
            brandMapper.insertSyncHistory(history);

            log.debug("동기화 결과 저장 - brandId={}, status={}", result.getBrandId(), status);
        }
    }

    // ===== 재시도 =====

    /**
     * 수동 재시도 (FAILED 상태 브랜드)
     */
    public void retrySyncForBrand(Long id) {
        Brand brand = brandMapper.selectBrandById(id);
        if (brand == null) {
            throw new BrandException("브랜드를 찾을 수 없습니다: " + id);
        }
        if (!"FAILED".equals(brand.getSyncStatus())) {
            throw new BrandException("FAILED 상태의 브랜드만 재시도할 수 있습니다.");
        }
        if (brand.getSyncRetryCount() >= 3) {
            throw new BrandException("최대 재시도 횟수(3회)를 초과했습니다.");
        }

        // 재시도 횟수 증가
        brandMapper.incrementSyncRetryCount(id);

        // 외부 API 재호출 (TX 밖)
        BrandSyncResultDto result = brandSyncService.syncSingleBrand(brand);

        // 결과 반영 (TX#2)
        self.updateSyncResults(List.of(result));
    }

    // ===== 헬퍼 =====

    private Brand toBrandEntity(BrandSaveReqDto req) {
        Brand brand = new Brand();
        brand.setId(req.getId());
        brand.setBrandCode(req.getBrandCode());
        brand.setBrandName(req.getBrandName());
        brand.setBrandNameEn(req.getBrandNameEn());
        brand.setBrandDesc(req.getBrandDesc());
        brand.setUseYn(req.getUseYn());
        brand.setVersion(req.getVersion());
        brand.setRegId("admin");    // TODO: 실무에서는 SecurityContext에서 추출
        brand.setUpdId("admin");
        return brand;
    }
}
