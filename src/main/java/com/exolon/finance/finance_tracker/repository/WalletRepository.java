package com.exolon.finance.finance_tracker.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exolon.finance.finance_tracker.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
	
	List<Wallet> findAllByUserId(UUID userId);
	List<Wallet> findAllByUserIdAndIsActiveTrue(UUID userId);
	List<Wallet> findAllByUserIdAndIsActiveFalse(UUID userId);
	Optional<Wallet> findByUserIdAndName(UUID userId, String name);
	Optional<Wallet> findByIdAndUserId(Long id, UUID userId);
	boolean existsByUserIdAndName(UUID userId, String name);
}
