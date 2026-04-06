package com.exolon.finance.finance_tracker.services;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.criteria.Predicate;

import com.exolon.finance.finance_tracker.dto.TransactionRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.exolon.finance.finance_tracker.dto.CategoryStatsDto;
import com.exolon.finance.finance_tracker.dto.TransactionDto;
import com.exolon.finance.finance_tracker.model.AppUser;
import com.exolon.finance.finance_tracker.model.Category;
import com.exolon.finance.finance_tracker.model.Transaction;
import com.exolon.finance.finance_tracker.model.TransactionType;
import com.exolon.finance.finance_tracker.model.Wallet;
import com.exolon.finance.finance_tracker.repository.TransactionRepository;
import com.exolon.finance.finance_tracker.repository.WalletRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional 
public class TransactionService {
    
    private final TransactionRepository repo;
    private final WalletService walletService; 
    private final CategoryService categoryService;
    private final UserService userService;
    private final WalletRepository walletRepository;

    public Page<TransactionDto> getAllTransactions(
            UUID userId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            TransactionType type,
            Long walletId,
            UUID categoryId,
            Pageable pageable) {

        // Build the dynamic WHERE clause
        Specification<Transaction> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. MUST match the logged-in user
            predicates.add(cb.equal(root.get("user").get("id"), userId));

            // 2. Date Range Filter
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("transactionDate"), startDate));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("transactionDate"), endDate));
            }

            // 3. Type Filter (INCOME, EXPENSE, TRANSFER)
            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }

            // 4. Wallet Filter (Checks BOTH source and target wallets for transfers!)
            if (walletId != null) {
                Predicate sourceWallet = cb.equal(root.get("wallet").get("id"), walletId);
                Predicate targetWallet = cb.equal(root.get("targetWallet").get("id"), walletId);
                predicates.add(cb.or(sourceWallet, targetWallet));
            }

            // 5. Category Filter
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }

            // Combine all predicates with AND
            // Even though we have written size = 0 the toArray method creates the array of required size, this is the modern java
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // Fetch paginated AND filtered data
        Page<Transaction> transactions = repo.findAll(spec, pageable);

        // Map to DTO
        return transactions.map(this::mapToDto);
    }
    
    public TransactionDto createTransaction(UUID userId, TransactionRequestDto req) {
        Wallet wallet = walletService.getByIdAndUserId(req.getWalletId(), userId);
        AppUser user = userService.getById(userId);
        
        Category category = null;
        if (req.getCategoryId() != null) {
            category = categoryService.getByIdAndUserId(req.getCategoryId(), userId);
        }

        Wallet targetWallet = null;
        if (req.getType() == TransactionType.TRANSFER && req.getTargetWalletId() != null) {
            targetWallet = walletService.getByIdAndUserId(req.getTargetWalletId(), userId);
      
            if (wallet.getId().equals(targetWallet.getId())) {
                throw new IllegalArgumentException("Cannot transfer to the same wallet");
            }
        }

        handleBalanceUpdate(wallet, targetWallet, req.getAmount(), req.getType());

        Transaction transaction = Transaction.builder()
                .amount(req.getAmount())
                .description(req.getDescription())
                .transactionDate(req.getTransactionDate())
                .type(req.getType())
                .wallet(wallet)
                .category(category)
                .user(user)
                .targetWallet(targetWallet)
                .createdAt(Instant.now())
                .build();
        
        Transaction savedT = repo.save(transaction);
        
        return mapToDto(savedT);
    }


    private void handleBalanceUpdate(Wallet sourceWallet, Wallet targetWallet, BigDecimal amount, TransactionType type) {
        switch (type) {
            case EXPENSE:
                sourceWallet.setBalance(sourceWallet.getBalance().subtract(amount));
                break;
            case INCOME:
                sourceWallet.setBalance(sourceWallet.getBalance().add(amount));
                break;
            case TRANSFER:
                if (targetWallet == null) throw new IllegalArgumentException("Target wallet required for transfer");
                sourceWallet.setBalance(sourceWallet.getBalance().subtract(amount));
                targetWallet.setBalance(targetWallet.getBalance().add(amount));
                walletRepository.save(targetWallet); 
                break;
        }
        walletRepository.save(sourceWallet); 
    }

    private TransactionDto mapToDto(Transaction t) {
        return TransactionDto.builder()
                .id(t.getId())
                .amount(t.getAmount())
                .description(t.getDescription())
                .transactionDate(t.getTransactionDate())
                .type(t.getType())
                .walletName(t.getWallet().getName())
                .categoryName(t.getCategory() != null ? t.getCategory().getName() : null)
                .targetWalletName(t.getTargetWallet() != null ? t.getTargetWallet().getName() : null)
                .createdAt(t.getCreatedAt())
                .build();
    }

	public List<CategoryStatsDto> getCategoryStats(UUID userId, LocalDateTime startDate, LocalDateTime endDate) {
		
		return repo.getCategoryStats(userId, startDate, endDate);
	}
}