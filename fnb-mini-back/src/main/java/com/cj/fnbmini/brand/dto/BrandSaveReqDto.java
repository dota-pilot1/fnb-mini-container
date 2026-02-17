package com.cj.fnbmini.brand.dto;

import lombok.Data;

@Data
public class BrandSaveReqDto {
    private Long id;
    private String brandCode;
    private String brandName;
    private String brandNameEn;
    private String brandDesc;
    private String useYn;
    private Integer version;    // 낙관적 락용 (수정 시 필수)
    private String status;      // C (create) / U (update) / D (delete)
}
