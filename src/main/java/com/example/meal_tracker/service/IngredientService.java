package com.example.meal_tracker.service;

import com.example.meal_tracker.dto.response.IngredientResponse;
import com.example.meal_tracker.dto.request.IngredientRequest;
import com.example.meal_tracker.exception.IngredientManagementException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IngredientService {

    IngredientResponse createIngredient(IngredientRequest request) throws IngredientManagementException;

    Page<IngredientResponse> getAllIngredients(Pageable pageable);

    IngredientResponse getIngredientById(Long id) throws IngredientManagementException;

    IngredientResponse updateIngredient(Long id, IngredientRequest request) throws IngredientManagementException;

    void deleteIngredient(Long id) throws IngredientManagementException;
}
