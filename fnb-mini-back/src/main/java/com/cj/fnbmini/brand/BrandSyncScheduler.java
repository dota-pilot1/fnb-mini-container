package com.cj.fnbmini.brand;

import com.cj.fnbmini.brand.entity.Brand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 동기화 실패 자동 재시도 스케줄러
 *
 * 5분마다 FAILED 상태 & retry_count < 3인 브랜드를 자동 재시도
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BrandSyncScheduler {

    private final BrandService brandService;
    private final BrandMapper brandMapper;

    @Scheduled(fixedRate = 300_000)  // 5분마다
    public void retryFailedSyncs() {
        List<Brand> failed = brandMapper.selectFailedSyncBrands();
        if (failed.isEmpty()) {
            return;
        }

        log.info("[스케줄러] 동기화 실패 자동 재시도 - {}건 발견", failed.size());

        for (Brand brand : failed) {
            try {
                brandService.retrySyncForBrand(brand.getId());
                log.info("[스케줄러] 재시도 완료 - brand '{}'", brand.getBrandCode());
            } catch (Exception e) {
                log.warn("[스케줄러] 재시도 실패 - brand '{}': {}", brand.getBrandCode(), e.getMessage());
            }
        }
    }
}
