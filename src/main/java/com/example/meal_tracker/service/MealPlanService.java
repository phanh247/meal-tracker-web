package com.example.meal_tracker.service;

import com.example.meal_tracker.dto.request.AddMealPlanRequest;
import com.example.meal_tracker.dto.request.AddMealRequest;
import com.example.meal_tracker.dto.request.UpdateMealPlanRequest;
import com.example.meal_tracker.dto.response.MealPlanResponse;
import com.example.meal_tracker.dto.response.MealResponse;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MealPlanService {
    void addNewMealPlan(AddMealPlanRequest addMealPlanRequest) throws BadRequestException;
    Page<MealPlanResponse> getMealPlansByUserId(Pageable pageable, Long userId);
    void updateMealPlan(Long mealPlanId, UpdateMealPlanRequest updateMealPlanRequest) throws BadRequestException;
    void deleteMealPlan(Long mealPlanId) throws BadRequestException;
    MealResponse addNewMeal(AddMealRequest request);
}
