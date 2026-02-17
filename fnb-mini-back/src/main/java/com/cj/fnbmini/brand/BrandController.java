package com.cj.fnbmini.brand;

import com.cj.fnbmini.brand.dto.BrandSaveReqDto;
import com.cj.fnbmini.brand.dto.BrandSearchDto;
import com.cj.fnbmini.brand.entity.Brand;
import com.cj.fnbmini.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    /** 목록 조회 (동적 검색) */
    @GetMapping
    public ApiResponse<List<Brand>> list(BrandSearchDto searchDto) {
        return ApiResponse.ok(brandService.getBrandList(searchDto));
    }

    /** 단건 조회 */
    @GetMapping("/{id}")
    public ApiResponse<Brand> detail(@PathVariable Long id) {
        return ApiResponse.ok(brandService.getBrandById(id));
    }

    /** 저장 (C/U/D 배치) */
    @PostMapping
    public ApiResponse<Void> save(@RequestBody List<BrandSaveReqDto> list) {
        brandService.saveBrands(list);
        return ApiResponse.ok(null, "저장되었습니다.");
    }

    /** 수동 재시도 */
    @PostMapping("/{id}/retry")
    public ApiResponse<Void> retry(@PathVariable Long id) {
        brandService.retrySyncForBrand(id);
        return ApiResponse.ok(null, "재시도가 요청되었습니다.");
    }

    /** 실패 목록 조회 */
    @GetMapping("/sync-failed")
    public ApiResponse<List<Brand>> failedList() {
        return ApiResponse.ok(brandService.getFailedSyncBrands());
    }
}
