package com.example.meal_tracker.service;

import com.example.meal_tracker.dto.request.AddMealRequest;
import com.example.meal_tracker.dto.response.MealResponse;
import com.example.meal_tracker.exception.NotFoundException;

import java.util.List;

public interface MealService {
    MealResponse addNewMeal(AddMealRequest request);
    List<MealResponse> getMeals();
    MealResponse getMealById(Long id);
    void updateMeal(Long id, AddMealRequest request) throws NotFoundException;
    void deleteMeal(Long id) throws NotFoundException;
}
