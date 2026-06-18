package com.app.financemanager.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class IncomeRequest {
    private Long id;
    private String name;
    private String icon;
    private String categoryName;
    private BigDecimal amount;
    private Long categoryId;
    private LocalDate date;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
