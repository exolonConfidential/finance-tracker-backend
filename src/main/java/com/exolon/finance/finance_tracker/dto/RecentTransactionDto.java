package com.exolon.finance.finance_tracker.dto;

import com.exolon.finance.finance_tracker.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecentTransactionDto {
    private Long id;
    private BigDecimal amount;
    private String description;
    private LocalDateTime transactionDate;
    private TransactionType type;
    private String categoryName;
    private String walletName;
    private String targetWalletName; // New Field

}