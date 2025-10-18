package com.example.meal_tracker.service;

import com.example.meal_tracker.dto.response.CategoryResponse;
import com.example.meal_tracker.exception.CategoryManagementException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryResponse addNewCategory(String categoryName) throws CategoryManagementException;
    Page<CategoryResponse> getAllCategories(Pageable pageable);
    void updateCategory(Long categoryId, String newCategoryName) throws CategoryManagementException;
    void deleteCategory(Long categoryId) throws CategoryManagementException;
}
