package com.cj.fnbmini.settlement.dto;

import lombok.Data;

@Data
public class SettlementSyncResultDto {
    private Long settlementId;
    private String syncType;        // SETTLE / REVERSE / CANCEL
    private boolean success;
    private String errorMessage;
    private String requestPayload;
    private String responsePayload;
}
