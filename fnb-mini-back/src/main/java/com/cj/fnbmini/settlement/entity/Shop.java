package com.cj.fnbmini.settlement.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Shop {
    private Long id;
    private String shopCode;
    private String shopName;
    private String useYn;
    private LocalDateTime createdAt;
}
