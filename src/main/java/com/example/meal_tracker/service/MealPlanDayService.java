package com.example.meal_tracker.service;

import com.example.meal_tracker.dto.request.AddMealPlanDayRequest;
import com.example.meal_tracker.dto.request.UpdateMealPlanDayRequest;
import com.example.meal_tracker.dto.response.MealPlanDayResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MealPlanDayService {
    MealPlanDayResponse addNewMealPlanDay(AddMealPlanDayRequest addMealPlanDayRequest) throws BadRequestException;

    Page<MealPlanDayResponse> getMealPlans(Pageable pageable, Long userId);

    void updateMealPlanDay(Long mealPlanDayId, UpdateMealPlanDayRequest updateMealPlanDayRequest)
            throws BadRequestException;

    void deleteMealPlanDay(Long mealPlanDayId) throws BadRequestException;
}
