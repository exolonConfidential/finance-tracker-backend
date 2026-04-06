package com.exolon.finance.finance_tracker.controllers;


import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exolon.finance.finance_tracker.dto.CategoryRequestDTO;
import com.exolon.finance.finance_tracker.dto.CategoryResponseDTO;
import com.exolon.finance.finance_tracker.dto.MessageDto;
import com.exolon.finance.finance_tracker.model.Category;
import com.exolon.finance.finance_tracker.services.CategoryService;
import com.exolon.finance.finance_tracker.utils.AuthUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final AuthUtil auth;
    
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories(Authentication authentication) {
        UUID userId = auth.getUserIdFromAuth(authentication);
        return ResponseEntity.ok(categoryService.getAllCategories(userId));
    }

    
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(
           @Valid @RequestBody CategoryRequestDTO categoryDTO, 
            Authentication authentication) {
        UUID userId = auth.getUserIdFromAuth(authentication);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.createCategory(userId, categoryDTO));
    }

   
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDto> deleteCategory(
            @PathVariable UUID id, 
            Authentication authentication) {
        UUID userId = auth.getUserIdFromAuth(authentication);
        Category category =  categoryService.deleteCategory(userId, id);
        return ResponseEntity.status(HttpStatus.OK)
        		.body(new MessageDto(category.getName() + " category deleted successfully"));
    }

}
