package com.example.meal_tracker.service;

import com.example.meal_tracker.dto.request.AddMealPlanItemRequest;
import com.example.meal_tracker.dto.request.UpdateMealPlanItemRequest;
import com.example.meal_tracker.dto.response.MealPlanItemResponse;

import java.time.LocalDate;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MealPlanItemService {
    MealPlanItemResponse addNewMealPlanItem(AddMealPlanItemRequest addMealPlanItemRequest) throws BadRequestException;

    Page<MealPlanItemResponse> getMealPlanItems(Pageable pageable, Long mealPlanId, LocalDate date);

    void updateMealPlanItem(Long mealPlanItemId, UpdateMealPlanItemRequest updateMealPlanItemRequest)
            throws BadRequestException;

    void deleteMealPlanItem(Long mealPlanItemId) throws BadRequestException;
}
