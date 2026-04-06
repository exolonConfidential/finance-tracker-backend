package com.exolon.finance.finance_tracker.controllers;


import com.exolon.finance.finance_tracker.dto.DashboardDto;
import com.exolon.finance.finance_tracker.services.DashboardService;
import com.exolon.finance.finance_tracker.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService service;
    private final AuthUtil authUtil;

    @GetMapping
    public ResponseEntity<DashboardDto> getDashboardStats(Authentication authentication) {
        UUID userId = authUtil.getUserIdFromAuth(authentication);
        return ResponseEntity.ok(service.getDashboardStats(userId));
    }
}
