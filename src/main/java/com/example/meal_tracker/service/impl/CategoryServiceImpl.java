package com.example.meal_tracker.service.impl;

import com.example.meal_tracker.dto.response.CategoryResponse;
import com.example.meal_tracker.entity.Category;
import com.example.meal_tracker.exception.CategoryManagementException;
import com.example.meal_tracker.repository.CategoryRepository;
import com.example.meal_tracker.service.CategoryService;
import com.example.meal_tracker.util.ConverterUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.meal_tracker.common.ErrorConstant.CATEGORY_EXISTED;
import static com.example.meal_tracker.common.ErrorConstant.CATEGORY_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse addNewCategory(String categoryName) throws CategoryManagementException {
        // Find if category already exists
        Optional<Category> existingCategory = categoryRepository.findByName(categoryName);
        if (existingCategory.isPresent()) {
            LOGGER.info("Category with name '{}' already exists.", categoryName);
            throw new CategoryManagementException(String.format(CATEGORY_EXISTED, categoryName));
        }

        Category category = ConverterUtil.convertToEntity(categoryName);
        categoryRepository.save(category);
        return ConverterUtil.convertToDto(category);
    }

    @Override
    public Page<CategoryResponse> getAllCategories(Pageable pageable) {
        pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(ConverterUtil::convertToDto);
    }

    @Override
    public void updateCategory(Long categoryId, String newCategoryName) throws CategoryManagementException {
        if (categoryRepository.findById(categoryId).isEmpty()) {
            LOGGER.info(CATEGORY_NOT_FOUND, categoryId);
            throw new CategoryManagementException(String.format(CATEGORY_NOT_FOUND, categoryId));
        }
        categoryRepository.findById(categoryId).map(category -> {
            category.setName(newCategoryName);
            return categoryRepository.save(category);
        });

    }

    @Override
    public void deleteCategory(Long categoryId) throws CategoryManagementException {
        if (categoryRepository.findById(categoryId).isEmpty()) {
            LOGGER.info(CATEGORY_NOT_FOUND, categoryId);
            throw new CategoryManagementException(String.format(CATEGORY_NOT_FOUND, categoryId));
        }
        categoryRepository.deleteById(categoryId);
    }
}
