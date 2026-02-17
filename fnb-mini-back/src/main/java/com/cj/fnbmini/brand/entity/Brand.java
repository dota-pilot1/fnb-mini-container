package com.cj.fnbmini.brand.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Brand {
    private Long id;
    private String brandCode;
    private String brandName;
    private String brandNameEn;
    private String brandDesc;
    private String useYn;

    // 동기화 상태 추적
    private String syncStatus;          // NONE / PENDING / SUCCESS / FAILED
    private Integer syncRetryCount;
    private LocalDateTime lastSyncAt;
    private String lastSyncError;

    // 낙관적 락
    private Integer version;

    // 감사 필드
    private String regId;
    private LocalDateTime regDttm;
    private String updId;
    private LocalDateTime updDttm;

    // 트랜잭션 분리 시 사용 (DB 미매핑)
    private transient String syncType;  // REGISTER / UPDATE
}
