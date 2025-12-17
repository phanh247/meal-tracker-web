package com.example.meal_tracker.service;

import com.example.meal_tracker.dto.request.AddMealPlanItemRequest;
import com.example.meal_tracker.dto.request.UpdateMealPlanItemRequest;
import com.example.meal_tracker.dto.response.ActiveMealPlanWithMealsResponse;
import com.example.meal_tracker.dto.response.MealPlanItemResponse;

import java.time.LocalDate;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MealPlanItemService {
    MealPlanItemResponse addNewMealPlanItem(AddMealPlanItemRequest addMealPlanItemRequest) throws BadRequestException;

    Page<MealPlanItemResponse> getMealPlanItems(Pageable pageable, Long mealPlanId, LocalDate date) throws BadRequestException;

    void updateMealPlanItem(Long mealPlanItemId, UpdateMealPlanItemRequest updateMealPlanItemRequest)
            throws BadRequestException;

    void deleteMealPlanItem(Long mealPlanItemId) throws BadRequestException;

    /**
     * Get active meal plan with all meals grouped by date
     * @param userId user id
     * @return ActiveMealPlanWithMealsResponse containing active meal plan and meals grouped by date
     * @throws BadRequestException if no active meal plan found
     */
    ActiveMealPlanWithMealsResponse getActiveMealPlanWithMeals(Long userId) throws BadRequestException;
}
