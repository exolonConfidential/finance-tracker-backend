package com.exolon.finance.finance_tracker.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.exolon.finance.finance_tracker.dto.WalletCashFlowDto;
import com.exolon.finance.finance_tracker.dto.WalletRequestDto;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.exolon.finance.finance_tracker.dto.WalletDto;
import com.exolon.finance.finance_tracker.services.WalletService;
import com.exolon.finance.finance_tracker.utils.AuthUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wallets")
public class WalletConroller {
	
	private final AuthUtil auth;
	private final WalletService walletService;
	
	@PostMapping
	public ResponseEntity<WalletDto> createWallet(@Valid @RequestBody WalletRequestDto req,
			Authentication authentication){
		UUID userId = auth.getUserIdFromAuth(authentication);
		WalletDto res = walletService.createWallet(userId, req);
		return ResponseEntity.status(HttpStatus.CREATED).body(res);
	}
	
	@GetMapping
	public ResponseEntity<List<WalletDto>> getWallets(Authentication authentication){
		UUID userId = auth.getUserIdFromAuth(authentication);
		List<WalletDto> res = walletService.getAll(userId);
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}

	//Toggle Endpoint
	@PatchMapping("/{id}/toggle")
	public ResponseEntity<WalletDto> updateStatus(Authentication authentication, @PathVariable Long id){
		UUID userId = auth.getUserIdFromAuth(authentication);
		WalletDto wallet = walletService.toggleWalletStatus(id, userId);
		return ResponseEntity.ok(wallet);
	}

	@GetMapping("/analytics")
	public ResponseEntity<List<WalletCashFlowDto>> getWalletAnalytics(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
			Authentication authentication) {

		UUID userId = auth.getUserIdFromAuth(authentication);
		List<WalletCashFlowDto> analytics = walletService.getWalletAnalytics(userId, startDate, endDate);

		return ResponseEntity.ok(analytics);
	}
}
