package com.exolon.finance.finance_tracker.dto;


import java.math.BigDecimal;

import com.exolon.finance.finance_tracker.model.CategoryType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDTO {
	
	@NotBlank(message = "category name is required")
	private String name;
	
	@NotNull(message = "category type is required")
	private CategoryType type;
	
	
	private BigDecimal budgetLimit;
	
}
