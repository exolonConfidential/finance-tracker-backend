package com.exolon.finance.finance_tracker.dto;

import com.exolon.finance.finance_tracker.model.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDto {

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    private String description;


    private LocalDateTime transactionDate;

    @NotNull(message = "Transaction type is required")
    private TransactionType type;

    @NotNull(message = "Wallet is required")
    private Long walletId;

    private Long targetWalletId;

    private UUID categoryId;
}
