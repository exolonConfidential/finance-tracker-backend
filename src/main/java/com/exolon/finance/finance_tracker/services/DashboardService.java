package com.exolon.finance.finance_tracker.services;

import com.exolon.finance.finance_tracker.dto.DashboardDto;
import com.exolon.finance.finance_tracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransactionRepository transactionRepository;

    public DashboardDto getDashboardStats(UUID userId){

        BigDecimal income = transactionRepository.sumIncomeByUser(userId);
        BigDecimal expense = transactionRepository.sumExpenseByUser(userId);

        // Convert nulls to Zero
        income = (income != null) ? income : BigDecimal.ZERO;
        expense = (expense != null) ? expense : BigDecimal.ZERO;

        // 2. Calculate Balance
        BigDecimal balance = income.subtract(expense);

        // 3. Fetch Recent Transactions and Map to DTO (Simplified mapping)
        var recent = transactionRepository.findRecentTransactionByUserId(userId, PageRequest.of(0,5));
        return DashboardDto.builder()
                .totalBalance(balance)
                .totalIncome(income)
                .totalExpense(expense)
                .recentTransactions(recent)
                .build();
    }

}
