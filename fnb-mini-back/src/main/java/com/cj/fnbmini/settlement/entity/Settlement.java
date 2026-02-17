package com.cj.fnbmini.settlement.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Settlement {
    private Long id;
    private Long shopId;
    private LocalDate salesDate;
    private Long totalAmt;
    private String status;          // READY / PROCESSING / SETTLED / FAILED
    private Integer version;
    private String idempotencyKey;
    private String sapSyncStatus;   // NONE / PENDING / SUCCESS / FAILED
    private String sapSyncError;
    private LocalDateTime settledAt;
    private String settledBy;
    private LocalDateTime createdAt;

    // 조인 필드
    private String shopCode;
    private String shopName;
}
