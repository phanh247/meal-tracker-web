package com.example.meal_tracker.util.converter;

import com.example.meal_tracker.dto.MealInstruction;
import com.example.meal_tracker.dto.request.AddMealPlanRequest;
import com.example.meal_tracker.dto.request.AddMealRequest;
import com.example.meal_tracker.dto.response.CategoryResponse;
import com.example.meal_tracker.dto.response.MealPlanResponse;
import com.example.meal_tracker.dto.response.MealResponse;
import com.example.meal_tracker.entity.Category;
import com.example.meal_tracker.entity.Meal;
import com.example.meal_tracker.entity.MealPlan;
import com.example.meal_tracker.exception.ConvertFailException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class DtoConverter {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Meal convertToEntity(AddMealRequest request) {
        return Meal.builder()
                .name(request.getMealName())
                .description(request.getMealDescription())
                .calories(request.getCalories())
                .imageUrl(request.getMealImageUrl())
                .mealInstructions(request.getMealInstructions())
                .cookingTime(request.getCookingTime())
                .servings(request.getServings())
                .nutrition(request.getNutrition())
                .build();
    }

    public static Category convertToEntity(String categoryName) {
        long now = System.currentTimeMillis();
        return Category.builder()
                .name(categoryName)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public static MealResponse convertToDto(Meal meal) {
        // Get category name
        List<String> categoryNames = meal.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toList());

        return MealResponse.builder()
                .id(meal.getId())
                .name(meal.getName())
                .description(meal.getDescription())
                .calories(meal.getCalories())
                .imageUrl(meal.getImageUrl())
                .mealInstructions(meal.getMealInstructions())
                .cookingTime(meal.getCookingTime())
                .servings(meal.getServings())
                .nutrition(meal.getNutrition())
                .categoryName(categoryNames)
                .build();
    }

    public static CategoryResponse convertToDto(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    public static MealPlan convertToEntity(AddMealPlanRequest request) {
        long now = System.currentTimeMillis();
        return MealPlan.builder()
                .name(request.mealPlanName)
                .userId(request.userId)
                .isSuggested(request.isSuggested)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public static MealPlanResponse convertToDto(MealPlan mealPlan) {
        return MealPlanResponse.builder()
                .id(mealPlan.getId())
                .name(mealPlan.getName())
                .isSuggested(mealPlan.getIsSuggested())
                .createdAt(mealPlan.getCreatedAt())
                .updatedAt(mealPlan.getUpdatedAt())
                .build();
    }
}
