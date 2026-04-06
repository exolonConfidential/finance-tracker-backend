package com.exolon.finance.finance_tracker.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.exolon.finance.finance_tracker.model.CategoryType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {

	private UUID id;
	private String name;
	private CategoryType type;
	private BigDecimal budgetLimit;
}
