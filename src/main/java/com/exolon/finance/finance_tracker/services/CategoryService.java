package com.exolon.finance.finance_tracker.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.exolon.finance.finance_tracker.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import com.exolon.finance.finance_tracker.dto.CategoryRequestDTO;
import com.exolon.finance.finance_tracker.dto.CategoryResponseDTO;
import com.exolon.finance.finance_tracker.exceptions.ItemAlreadyExistsException;
import com.exolon.finance.finance_tracker.exceptions.ResourceConflictException;
import com.exolon.finance.finance_tracker.exceptions.ResourceNotFoundException;
import com.exolon.finance.finance_tracker.model.AppUser;
import com.exolon.finance.finance_tracker.model.Category;
import com.exolon.finance.finance_tracker.model.CategoryType;
import com.exolon.finance.finance_tracker.repository.CategoryRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryService {
	
	private final CategoryRepository categoryRepo;
	private final TransactionRepository transactionRepository;
	
	public List<CategoryResponseDTO> getAllCategories(UUID userId) {
	    List<Category> categories = categoryRepo.findAllByUserId(userId);
	    
	    List<CategoryResponseDTO> responseCategories = categories.stream()
	            .map(category -> new CategoryResponseDTO(
	                    category.getId(),
	                    category.getName(),
	                    category.getType(),
	                    category.getBudgetLimit()
	            ))
	            .collect(Collectors.toList());
	    
	    return responseCategories;
	}
	
	public Category getByIdAndUserId(UUID categoryId, UUID userId) {
		return categoryRepo.findByIdAndUserId(categoryId, userId).get();
	}
	
	public CategoryResponseDTO createCategory(UUID userId, CategoryRequestDTO reqCategory) {
		if(categoryRepo.existsByUserIdAndName(userId, reqCategory.getName())) {
			throw new ItemAlreadyExistsException("Category " + reqCategory.getName() + " already exists");
		}
		AppUser user = new AppUser();
		user.setId(userId);
		Category category = new Category();
		category.setName(reqCategory.getName());
		category.setType((CategoryType)reqCategory.getType());
		category.setBudgetLimit(reqCategory.getBudgetLimit());
		category.setUser(user);
		
		Category saved = categoryRepo.save(category);
		
		CategoryResponseDTO resCat = new CategoryResponseDTO(saved.getId(),saved.getName(),saved.getType(),saved.getBudgetLimit());
		
		
		return resCat;
		
	}
	
	public Category deleteCategory(UUID userId, UUID categoryId) {
		if(transactionRepository.existsByUserIdAndCategoryId(userId, categoryId)){
			throw new ResourceConflictException("Transactions exists for this category");
		}
		Category category = categoryRepo.findByIdAndUserId(categoryId, userId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found"));
		categoryRepo.delete(category);
		return category;
	}
	
}
