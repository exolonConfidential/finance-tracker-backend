package com.exolon.finance.finance_tracker.dto;

import com.exolon.finance.finance_tracker.model.WalletType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class WalletRequestDto {

    @NotBlank(message = "Wallet name is required")
    @Size(min = 2, max = 50, message = "Wallet name must be between 2 and 50 characters")
    private String name;

    @NotNull(message = "Wallet type is required (CASH, BANK_ACCOUNT, CREDIT_CARD, INVESTMENT, SAVINGS)")
    private WalletType type;
}
