package com.cj.fnbmini.settlement.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SettlementLedger {
    private Long id;
    private Long settlementId;
    private Long shopId;
    private LocalDate salesDate;
    private String ledgerType;  // NORMAL / REVERSE
    private Long cashAmt;
    private Long cardAmt;
    private Long easyAmt;
    private Long totalAmt;
    private String delYn;
    private LocalDateTime createdAt;
}
