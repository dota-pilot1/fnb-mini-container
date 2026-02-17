package com.cj.fnbmini.brand.dto;

import lombok.Data;

@Data
public class BrandSearchDto {
    private String brandCode;
    private String brandName;
    private String useYn;
    private String syncStatus;
}
