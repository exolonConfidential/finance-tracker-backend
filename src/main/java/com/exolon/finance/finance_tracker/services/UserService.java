package com.exolon.finance.finance_tracker.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exolon.finance.finance_tracker.dto.UserSignupDto;
import com.exolon.finance.finance_tracker.exceptions.ItemAlreadyExistsException;
import com.exolon.finance.finance_tracker.exceptions.UserNotFoundException;
import com.exolon.finance.finance_tracker.model.AppUser;
import com.exolon.finance.finance_tracker.model.Role;
import com.exolon.finance.finance_tracker.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
	
	private final UserRepository userRepo;
	
	private PasswordEncoder passwordEncoder;
	
	public AppUser getUserByEmail(String email) {
		AppUser user = userRepo.findByEmail(email)
				.orElseThrow(()-> new UserNotFoundException("User does not exists"));
		return user;
	}
	
	public AppUser createUser(UserSignupDto req) {
		Optional<AppUser> user = userRepo.findByEmail(req.getEmail());
		if(user.isPresent()) {
			 throw new ItemAlreadyExistsException("User with email " + req.getEmail() + " already exists");
		}
		
		String password = passwordEncoder.encode(req.getPassword());
		AppUser newUser = AppUser.builder()
				.firstName(req.getFirstName())
				.lastName(req.getLastName())
				.email(req.getEmail())
				.password(password)
				.role(Role.USER)
				.country(req.getCountry())
				.currency(req.getCurrency())
				.build();
		AppUser savedUser = userRepo.save(newUser);
		return savedUser;
	}
	
	public AppUser getById(UUID userId) {
		return userRepo.findById(userId).get();
	}
}
