package com.exolon.finance.finance_tracker.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.exolon.finance.finance_tracker.dto.RecentTransactionDto;
import com.exolon.finance.finance_tracker.dto.WalletCashFlowDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.exolon.finance.finance_tracker.dto.CategoryStatsDto;
import com.exolon.finance.finance_tracker.model.Transaction;
import com.exolon.finance.finance_tracker.model.TransactionType;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
	
	Page<Transaction> findAllByUserId(UUID userId, Pageable pageable);

    
    Page<Transaction> findAllByUserIdAndType(UUID userId, TransactionType type, Pageable pageable);


    Page<Transaction> findAllByUserIdAndCategoryId(UUID userId, UUID categoryId, Pageable pageable);

    boolean existsByUserIdAndCategoryId(UUID userId, UUID categoryId);

    
    // Spring automatically applies the Pageable limits (LIMIT 10 OFFSET x) to this query
    @Query("SELECT r FROM Transaction r WHERE (r.wallet.id = :walletId OR r.targetWallet.id = :walletId) AND r.user.id = :userId")
    Page<Transaction> findAllByWalletId(@Param("walletId") Long walletId, @Param("userId") UUID userId, Pageable pageable);

    
    Page<Transaction> findAllByUserIdAndTransactionDateBetween(
        UUID userId, 
        LocalDateTime startDate, 
        LocalDateTime endDate, 
        Pageable pageable
    );
    
 // "Sum up all amounts, grouped by category name, for this user, in this date range"
    @Query("""
			SELECT new com.exolon.finance.finance_tracker.dto.CategoryStatsDto(c.name, c.type, SUM(t.amount))
			FROM Transaction t
			JOIN t.category c
			WHERE t.user.id = :userId
			AND (t.type = 'EXPENSE' OR t.type = 'INCOME')
			AND t.transactionDate BETWEEN :startDate AND :endDate
			GROUP BY c.name
			\t""")
    List<CategoryStatsDto> getCategoryStats(
        @Param("userId") UUID userId, 
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate
    );

    // this is called using ProjectionDto (we only select what's needed into java object)
    @Query("""
            SELECT new com.exolon.finance.finance_tracker.dto.RecentTransactionDto(
            t.id, t.amount, t.description, t.transactionDate, t.type, c.name, w.name, tw.name
            )
            FROM Transaction t
            LEFT JOIN t.category c
            LEFT JOIN t.targetWallet tw
            JOIN t.wallet w
            WHERE t.user.id = :userId
            ORDER BY t.transactionDate DESC
            """)
    List<RecentTransactionDto> findRecentTransactionByUserId(@Param("userId") UUID userId, Pageable pageable);

    @Query("SELECT SUM(t.amount) FROM Transaction t JOIN t.category c WHERE t.user.id = :userId AND t.type = 'EXPENSE' ")
    BigDecimal sumExpenseByUser(@Param("userId") UUID userId);

    @Query("SELECT SUM(t.amount) FROM Transaction t JOIN t.category c WHERE t.user.id = :userId AND t.type = 'INCOME' ")
    BigDecimal sumIncomeByUser(@Param("userId") UUID userId);

    // Calculate accurate In/Out including Transfers
    @Query("""
       SELECT new com.exolon.finance.finance_tracker.dto.WalletCashFlowDto(
           w.id,
           w.name,
           (SELECT COALESCE(SUM(tIn.amount), 0) FROM Transaction tIn
            WHERE tIn.user.id = :userId
              AND tIn.transactionDate >= :startDate
              AND tIn.transactionDate <= :endDate
              AND ((tIn.type = 'INCOME' AND tIn.wallet.id = w.id)
                   OR (tIn.type = 'TRANSFER' AND tIn.targetWallet.id = w.id))),
           (SELECT COALESCE(SUM(tOut.amount), 0) FROM Transaction tOut
            WHERE tOut.user.id = :userId
              AND tOut.transactionDate >= :startDate
              AND tOut.transactionDate <= :endDate
              AND ((tOut.type = 'EXPENSE' AND tOut.wallet.id = w.id)
                   OR (tOut.type = 'TRANSFER' AND tOut.wallet.id = w.id))),
            w.isActive
       )
       FROM Wallet w
       WHERE w.user.id = :userId
       """)
    List<WalletCashFlowDto> getWalletCashFlow(
            @Param("userId") UUID userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
