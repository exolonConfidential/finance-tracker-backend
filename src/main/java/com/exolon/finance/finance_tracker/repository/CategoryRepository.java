package com.exolon.finance.finance_tracker.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exolon.finance.finance_tracker.model.Category;
import com.exolon.finance.finance_tracker.model.CategoryType;

public interface CategoryRepository extends JpaRepository<Category,UUID>{
	
	 List<Category> findAllByUserId(UUID userId);
	 List<Category> findAllByUserIdAndType(UUID userId, CategoryType type);
	 boolean existsByUserIdAndName(UUID userId, String name);
	 Optional<Category> findByIdAndUserId(UUID id, UUID userId);
}
