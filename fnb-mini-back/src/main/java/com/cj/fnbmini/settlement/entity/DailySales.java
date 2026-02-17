package com.cj.fnbmini.settlement.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DailySales {
    private Long id;
    private Long shopId;
    private LocalDate salesDate;
    private Long cashAmt;
    private Long cardAmt;
    private Long easyAmt;
    private Long totalAmt;
    private LocalDateTime createdAt;

    // 조인 필드
    private String shopCode;
    private String shopName;
}
