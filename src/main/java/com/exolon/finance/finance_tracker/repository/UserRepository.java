package com.exolon.finance.finance_tracker.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exolon.finance.finance_tracker.model.AppUser;

public interface UserRepository extends JpaRepository<AppUser,UUID>{
	
	Optional<AppUser> findByEmail(String email);
	Optional<AppUser> findById(UUID id);
}
