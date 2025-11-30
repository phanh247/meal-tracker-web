package com.example.meal_tracker.service;

import com.example.meal_tracker.dto.request.AddMealRequest;
import com.example.meal_tracker.dto.response.MealResponse;
import com.example.meal_tracker.exception.InvalidDataException;
import com.example.meal_tracker.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MealService {
    MealResponse addNewMeal(AddMealRequest request, MultipartFile imageFile) throws NotFoundException, IOException;
    Page<MealResponse> getMeals(Pageable pageable);
    MealResponse getMealById(Long id) throws NotFoundException;
    MealResponse updateMeal(Long id, AddMealRequest request, MultipartFile imageFile)
    throws NotFoundException, IOException, InvalidDataException;
    void deleteMeal(Long id) throws NotFoundException;
    Page<MealResponse> filterMeals(String categoryName, String mealName, Double minCalories, Double maxCalories,
                                   String ingredientName, Pageable pageable) throws NotFoundException;
    List<MealResponse> recommendSimilarMeals(Long mealId, int limit) throws NotFoundException;
}
