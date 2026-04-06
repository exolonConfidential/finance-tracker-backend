package com.exolon.finance.finance_tracker.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.exolon.finance.finance_tracker.dto.WalletCashFlowDto;
import com.exolon.finance.finance_tracker.dto.WalletRequestDto;
import com.exolon.finance.finance_tracker.exceptions.ResourceNotFoundException;
import com.exolon.finance.finance_tracker.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import com.exolon.finance.finance_tracker.dto.WalletDto;
import com.exolon.finance.finance_tracker.exceptions.ItemAlreadyExistsException;
import com.exolon.finance.finance_tracker.model.AppUser;
import com.exolon.finance.finance_tracker.model.Wallet;
import com.exolon.finance.finance_tracker.repository.WalletRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalletService {
	
	private final WalletRepository walletRepo;
	private final TransactionRepository transactionRepository;
	
	@Transactional
	public WalletDto createWallet(UUID userId, WalletRequestDto req) {
		if(walletRepo.existsByUserIdAndName(userId, req.getName()	)){
			throw new ItemAlreadyExistsException("Wallet " + req.getName()+ " already exists");
		}
		AppUser user = new AppUser();
		user.setId(userId);
		Wallet wallet = new Wallet();
			wallet.setName(req.getName());
			wallet.setBalance(BigDecimal.ZERO);
			wallet.setType(req.getType());
			wallet.setUser(user);
		Wallet savedWallet = walletRepo.save(wallet);
		return 	 WalletDto.builder()
				.id(savedWallet.getId())
				.name(savedWallet.getName())
				.balance(savedWallet.getBalance())
				.type(savedWallet.getType())
				.isActive(savedWallet.isActive())
				.build();
	}
	
	public List<WalletDto> getAll(UUID userId){
		List<Wallet> wallets = walletRepo.findAllByUserId(userId);

		return wallets.stream()
				.map(wallet -> new WalletDto(
						wallet.getId(),
						wallet.getName(),
						wallet.getBalance(),
						wallet.getType(),
						wallet.isActive()
					)
				)
				.collect(Collectors.toList());

	}
	
	public Wallet getByIdAndUserId(Long id, UUID userId) {
		return walletRepo.findByIdAndUserId(id, userId)
				.orElseThrow(()-> new ResourceNotFoundException("Wallet not found or access denied"));
	}

	public WalletDto toggleWalletStatus(Long id, UUID userId){
		Wallet wallet = this.getByIdAndUserId(id, userId);
		wallet.setActive(!wallet.isActive());
		Wallet savedWallet = walletRepo.save(wallet);

		return WalletDto.builder()
				.id(savedWallet.getId())
				.name(savedWallet.getName())
				.balance(savedWallet.getBalance())
				.type(savedWallet.getType())
				.isActive(savedWallet.isActive())
				.build();
	}

	public List<WalletCashFlowDto> getWalletAnalytics(UUID userId, LocalDateTime startDate, LocalDateTime endDate) {
		return transactionRepository.getWalletCashFlow(userId, startDate, endDate);
	}
}
