package com.example.meal_tracker.service;

import com.example.meal_tracker.dto.request.AddMealPlanRequest;
import com.example.meal_tracker.dto.request.UpdateMealPlanRequest;
import com.example.meal_tracker.dto.response.MealPlanResponse;
import com.example.meal_tracker.exception.NotFoundException;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MealPlanService {
    MealPlanResponse addNewMealPlan(AddMealPlanRequest addMealPlanRequest) throws BadRequestException;

    Page<MealPlanResponse> getMealPlans(Pageable pageable, Long userId);

    MealPlanResponse getMealPlanById(Long id) throws NotFoundException;

    void updateMealPlan(Long mealPlanId, UpdateMealPlanRequest updateMealPlanRequest) throws BadRequestException;

    void deleteMealPlan(Long mealPlanId) throws BadRequestException;
}
