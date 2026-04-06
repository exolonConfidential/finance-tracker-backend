package com.exolon.finance.finance_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletCashFlowDto {
    private Long walletId;
    private String walletName;
    private BigDecimal totalIn;
    private BigDecimal totalOut;
    private Boolean walletIsActive;

}
