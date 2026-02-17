package com.cj.fnbmini.settlement.dto;

import lombok.Data;

@Data
public class SettlementReqDto {
    private Long shopId;
    private String salesDate;
    private String idempotencyKey;
}
