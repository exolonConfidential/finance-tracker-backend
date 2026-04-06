package com.exolon.finance.finance_tracker.controllers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.exolon.finance.finance_tracker.dto.TransactionRequestDto;
import com.exolon.finance.finance_tracker.model.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exolon.finance.finance_tracker.dto.CategoryStatsDto;
import com.exolon.finance.finance_tracker.dto.TransactionDto;
import com.exolon.finance.finance_tracker.services.TransactionService;
import com.exolon.finance.finance_tracker.utils.AuthUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
	private final TransactionService service;
	private final AuthUtil authUtil;

	@GetMapping
	public ResponseEntity<Page<TransactionDto>> getAllTransactions(
			Authentication auth,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
			@RequestParam(required = false) TransactionType type,
			@RequestParam(required = false) Long walletId,
			@RequestParam(required = false) UUID categoryId,
			@PageableDefault(
					page = 0,
					size = 10,
					sort = "transactionDate",
					direction = Sort.Direction.DESC
			) Pageable pageable
	){

		UUID userId = authUtil.getUserIdFromAuth(auth);

		Page<TransactionDto> transacPage = service.getAllTransactions(
				userId, startDate, endDate, type, walletId, categoryId, pageable
		);

		return ResponseEntity.status(HttpStatus.OK).body(transacPage);
	}
	
	@PostMapping
	public ResponseEntity<TransactionDto> createNewTransaction(
			@Valid @RequestBody TransactionRequestDto req,
			Authentication auth
			){
		UUID userId = authUtil.getUserIdFromAuth(auth);
		return ResponseEntity.status(HttpStatus.CREATED).body(service.createTransaction(userId, req));
	}
	
	// GET /api/transactions/stats?startDate=...&endDate=...
    @GetMapping("/stats")
    public ResponseEntity<List<CategoryStatsDto>> getTransactionStats(
            Authentication auth,
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate
            ) {
        UUID userId = authUtil.getUserIdFromAuth(auth);
        
        List<CategoryStatsDto> stats = service.getCategoryStats(userId, startDate, endDate);
        
        return ResponseEntity.ok(stats);
    }
}
