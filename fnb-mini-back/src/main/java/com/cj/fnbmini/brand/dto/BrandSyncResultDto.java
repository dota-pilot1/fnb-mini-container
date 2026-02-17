package com.cj.fnbmini.brand.dto;

import lombok.Data;

@Data
public class BrandSyncResultDto {
    private Long brandId;
    private boolean success;
    private String syncType;        // REGISTER / UPDATE
    private String requestPayload;
    private String responsePayload;
    private String errorMessage;
}
