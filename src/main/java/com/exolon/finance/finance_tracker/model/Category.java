package com.exolon.finance.finance_tracker.model;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="category", uniqueConstraints = {
	@UniqueConstraint (columnNames = {"user_id","name"})
})
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@NotBlank(message = "please provide the category name")
	@Column(nullable = false)
	private String name;
	
	@Enumerated(EnumType.STRING)
	@NotNull(message = "category type cannot be null")
	@Column(nullable = false, length = 50)
	private CategoryType type;
	
	@Nullable
	@Column(name = "budget_limit", precision = 19, scale = 2, nullable = true)
	private BigDecimal budgetLimit;
	
	

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private AppUser user;
	
}
