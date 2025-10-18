package com.example.meal_tracker.service;

import com.example.meal_tracker.dto.request.AddMealRequest;
import com.example.meal_tracker.dto.response.MealResponse;
import com.example.meal_tracker.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MealService {
    MealResponse addNewMeal(AddMealRequest request) throws NotFoundException;
    Page<MealResponse> getMeals(Pageable pageable);
    MealResponse getMealById(Long id) throws NotFoundException;
    void updateMeal(Long id, AddMealRequest request) throws NotFoundException;
    void deleteMeal(Long id) throws NotFoundException;
    Page<MealResponse> filterMeals(String categoryName, String mealName, float calories, Pageable pageable) throws NotFoundException;
}
