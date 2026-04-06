package com.exolon.finance.finance_tracker.dto;


import java.math.BigDecimal;

import com.exolon.finance.finance_tracker.model.WalletType;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletDto {
	
	private Long id;
	
    @NotBlank(message = "Wallet name is required")
    @Size(min = 2, max = 50, message = "Wallet name must be between 2 and 50 characters")
    private String name;

    @NotNull(message = "Balance is required")
    @Min(value = 0, message = "Initial balance cannot be negative")
    private BigDecimal balance;

    @NotNull(message = "Wallet type is required (CASH, BANK_ACCOUNT, CREDIT_CARD, INVESTMENT, SAVINGS)")
    private WalletType type;

    /*
    Even though you named your variable isActive in Java, the library that Spring Boot uses to convert
    Java objects into JSON (Jackson) has a quirk with the word "is". When it sees a getter like getIsActive()
    or isActive(), it strips the "is" away and sends it to the frontend simply as active: true.
    So, when your React code checks wallet.isActive, it returns undefined (which evaluates to false in JavaScript),
    causing the card to grey out!
     */
    @JsonProperty("isActive")
    private boolean isActive;
    
}
