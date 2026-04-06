package com.exolon.finance.finance_tracker.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.exolon.finance.finance_tracker.model.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor 
public class CategoryStatsDto {

    private String categoryName;
    private CategoryType type;
    private BigDecimal totalAmount;
}
