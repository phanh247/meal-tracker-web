package com.example.meal_tracker.util;

import com.example.meal_tracker.dto.request.AddMealPlanRequest;

import java.sql.Date;

import com.example.meal_tracker.dto.request.AddMealPlanDayRequest;
import com.example.meal_tracker.dto.request.AddMealRequest;
import com.example.meal_tracker.dto.response.CategoryResponse;
import com.example.meal_tracker.dto.response.MealPlanDayResponse;
import com.example.meal_tracker.dto.response.MealPlanResponse;
import com.example.meal_tracker.dto.response.MealResponse;
import com.example.meal_tracker.entity.Category;
import com.example.meal_tracker.entity.Meal;
import com.example.meal_tracker.entity.MealPlan;
import com.example.meal_tracker.entity.MealPlanDay;

public final class ConverterUtil {
    public static Meal convertToEntity(AddMealRequest request) {
        long now = System.currentTimeMillis();
        return Meal.builder()
                .name(request.getMealName())
                .description(request.getMealDescription())
                .imageUrl(request.getMealImageUrl())
                .calories(request.getCalories())
                .createdAt(now)
                .updatedAt(now)
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
        return MealResponse.builder()
                .id(meal.getId())
                .name(meal.getName())
                .imageUrl(meal.getImageUrl())
                .calories(meal.getCalories())
                .categoryName(meal.getCategory().getName())
                .description(meal.getDescription())
                .createdAt(meal.getCreatedAt())
                .updatedAt(meal.getUpdatedAt())
                .build();
    }

    public static MealPlanDayResponse convertToDto(MealPlanDay mealPlanDay) {
        return MealPlanDayResponse.builder()
                .mealPlanId(mealPlanDay.getMealPlanId())
                .date(mealPlanDay.getDate().toString())
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

    public static MealPlanDay convertToEntity(AddMealPlanDayRequest request) {
        return MealPlanDay.builder().mealPlanId(request.mealPlanId).date(Date.valueOf(request.date)).build();
    }
}
