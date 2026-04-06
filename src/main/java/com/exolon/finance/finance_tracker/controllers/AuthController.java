package com.exolon.finance.finance_tracker.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exolon.finance.finance_tracker.dto.AuthResponseDto;
import com.exolon.finance.finance_tracker.dto.UserLoginDto;
import com.exolon.finance.finance_tracker.dto.UserSignupDto;
import com.exolon.finance.finance_tracker.model.AppUser;
import com.exolon.finance.finance_tracker.repository.UserRepository;
import com.exolon.finance.finance_tracker.security.JwtService;
import com.exolon.finance.finance_tracker.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final JwtService jwtService;
	private final UserService userService;
	private final AuthenticationManager authManager;
	private final UserRepository userRepo;

	@PostMapping("/signup")
	public ResponseEntity<AuthResponseDto> signup(@Valid @RequestBody UserSignupDto req){
		AppUser savedUser = userService.createUser(req);
		String token = jwtService.generateToken(savedUser.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponseDto(token));
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody UserLoginDto req){
		// we are using the authManager to fetch the user and authenticate the user by checking the
		// password
		authManager.authenticate(
				 new UsernamePasswordAuthenticationToken(
						 	req.getEmail(),
						 	req.getPassword()
						 )
				); 
		AppUser user = userRepo.findByEmail(req.getEmail()).get();
		String token = jwtService.generateToken(user.getId());
		return ResponseEntity.status(HttpStatus.OK).body(new AuthResponseDto(token));
	}
}
