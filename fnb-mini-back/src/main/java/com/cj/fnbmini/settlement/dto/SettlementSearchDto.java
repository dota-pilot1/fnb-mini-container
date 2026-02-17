package com.cj.fnbmini.settlement.dto;

import lombok.Data;

@Data
public class SettlementSearchDto {
    private Long shopId;
    private String salesDateFrom;
    private String salesDateTo;
    private String status;
}
