package com.exolon.finance.finance_tracker.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.exolon.finance.finance_tracker.model.TransactionType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
	
	private Long id;
	
    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    private String description;

    
    private LocalDateTime transactionDate;

    @NotNull(message = "Transaction type is required")
    private TransactionType type; 
    
    @NotNull(message = "Wallet is required")
    private String walletName;

    private String targetWalletName;
 
    private String categoryName;

    @NotNull(message = "Creation date is required")
    private Instant createdAt;
}
