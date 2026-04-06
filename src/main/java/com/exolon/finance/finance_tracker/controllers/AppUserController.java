package com.exolon.finance.finance_tracker.controllers;

import com.exolon.finance.finance_tracker.dto.UserProfileDto;
import com.exolon.finance.finance_tracker.model.AppUser;
import com.exolon.finance.finance_tracker.services.UserService;
import com.exolon.finance.finance_tracker.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class AppUserController {

    private final AuthUtil authUtil;
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getUserProfile(Authentication authentication){
        UUID userId = authUtil.getUserIdFromAuth(authentication);
        AppUser user = userService.getById(userId);
        UserProfileDto userProfile = UserProfileDto.builder()
                .id(user.getId().toString())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .country(user.getCountry())
                .currency(user.getCurrency())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(userProfile);
    }

}
