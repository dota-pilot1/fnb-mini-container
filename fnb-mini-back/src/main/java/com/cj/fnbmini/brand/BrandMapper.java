package com.cj.fnbmini.brand;

import com.cj.fnbmini.brand.dto.BrandSearchDto;
import com.cj.fnbmini.brand.entity.Brand;
import com.cj.fnbmini.brand.entity.BrandSyncHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BrandMapper {

    List<Brand> selectBrandList(BrandSearchDto searchDto);

    Brand selectBrandById(Long id);

    int insertBrand(Brand brand);

    int updateBrand(Brand brand);

    /** 낙관적 락 - version 불일치 시 0행 반환 */
    int updateBrandWithVersion(Brand brand);

    int updateSyncStatus(@Param("id") Long id,
                         @Param("syncStatus") String syncStatus,
                         @Param("lastSyncError") String lastSyncError);

    /** 실패 브랜드 조회 (재시도 대상: retry_count < 3) */
    List<Brand> selectFailedSyncBrands();

    int insertSyncHistory(BrandSyncHistory history);

    int incrementSyncRetryCount(Long id);
}
