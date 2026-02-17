package com.cj.fnbmini.brand.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BrandSyncHistory {
    private Long id;
    private Long brandId;
    private String syncType;        // REGISTER / UPDATE
    private String syncStatus;      // PENDING / SUCCESS / FAILED
    private String requestPayload;
    private String responsePayload;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}
