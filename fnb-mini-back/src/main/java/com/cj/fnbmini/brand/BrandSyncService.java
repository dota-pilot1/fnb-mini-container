package com.cj.fnbmini.brand;

import com.cj.fnbmini.brand.dto.BrandSyncResultDto;
import com.cj.fnbmini.brand.entity.Brand;
import com.cj.fnbmini.external.ShopApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 외부 API 동기화 전담 서비스
 *
 * 핵심: 이 서비스의 메서드들은 @Transactional이 없다.
 * DB 커넥션을 점유하지 않고 외부 API를 호출한다.
 *
 * 실무 문제: AirstarBrandService.savePartenrBrands()에서
 * @Transactional 안에서 middlewareApiManager.sendRequest() 호출
 * → 커넥션 5~60초 점유
 *
 * 개선: BrandService가 TX#1(DB저장) 커밋 후 이 서비스 호출
 * → 커넥션 점유 없이 외부 API 호출
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BrandSyncService {

    private final ShopApiClient shopApiClient;

    /**
     * 외부 API 동기화 (TX 밖에서 호출됨 - DB 커넥션 점유 없음)
     */
    public List<BrandSyncResultDto> syncToExternalApi(List<Brand> brands) {
        List<BrandSyncResultDto> results = new ArrayList<>();

        for (Brand brand : brands) {
            BrandSyncResultDto result;

            if ("REGISTER".equals(brand.getSyncType())) {
                result = shopApiClient.registerBrand(brand);
            } else {
                result = shopApiClient.updateBrand(brand);
            }

            results.add(result);
            log.info("동기화 결과 - brand '{}': {}",
                    brand.getBrandCode(),
                    result.isSuccess() ? "SUCCESS" : "FAILED: " + result.getErrorMessage());
        }

        return results;
    }

    /**
     * 단건 동기화 (재시도용)
     */
    public BrandSyncResultDto syncSingleBrand(Brand brand) {
        return shopApiClient.registerBrand(brand);
    }
}
