package com.cj.fnbmini.settlement.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SettlementSyncHistory {
    private Long id;
    private Long settlementId;
    private String syncType;        // SETTLE / REVERSE / CANCEL
    private String syncStatus;      // SUCCESS / FAILED
    private String requestPayload;
    private String responsePayload;
    private String errorMessage;
    private LocalDateTime createdAt;
}
