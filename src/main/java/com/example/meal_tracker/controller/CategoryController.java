package com.example.meal_tracker.controller;

import com.example.meal_tracker.dto.request.AddCategoryRequest;
import com.example.meal_tracker.dto.response.CategoryResponse;
import com.example.meal_tracker.exception.CategoryManagementException;
import com.example.meal_tracker.exception.InvalidDataException;
import com.example.meal_tracker.service.CategoryService;
import com.example.meal_tracker.util.RequestValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addCategory(@RequestBody @Valid AddCategoryRequest request) {
        try {
            LOGGER.info("Received request to add new category: {}", request);
            RequestValidator.validateRequest(request);
            CategoryResponse response = categoryService.addNewCategory(request.getCategoryName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error("Error adding new category: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public  ResponseEntity<Page<CategoryResponse>> findAllBooks(@PageableDefault(size = 10) Pageable pageable) {
        LOGGER.info("Retrieving all category.......");
        return ResponseEntity.ok(categoryService.getAllCategories(pageable));
    }

    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id, @RequestBody AddCategoryRequest request) {
        try {
            LOGGER.info("Received request to update category id: {}", id);
            RequestValidator.validateRequest(request);
            categoryService.updateCategory(id, request.getCategoryName());
            return ResponseEntity.ok(true);
        } catch (CategoryManagementException | InvalidDataException e) {
            LOGGER.error("Error updating category: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        try {
            LOGGER.info("Received request to delete category with id: {}", categoryId);
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.ok(true);
        } catch (CategoryManagementException e) {
            LOGGER.error("Error deleting category: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
