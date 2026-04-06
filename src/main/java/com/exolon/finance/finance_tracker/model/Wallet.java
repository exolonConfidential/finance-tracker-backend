package com.exolon.finance.finance_tracker.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wallets", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "user_id"})
})
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Wallet name is required")
    @Size(min = 2, max = 50, message = "Wallet name must be between 2 and 50 characters")
    @Column(nullable = false)
    private String name;

   
    @NotNull(message = "Balance cannot be null")
    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO; 

    @NotNull(message = "Wallet type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WalletType type; 

    
    private boolean isActive = true;

   
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore 
    private AppUser user;
}
