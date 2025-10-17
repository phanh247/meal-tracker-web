package com.example.meal_tracker.util;

import com.example.meal_tracker.dto.request.AddMealRequest;
import com.example.meal_tracker.dto.response.CategoryResponse;
import com.example.meal_tracker.dto.response.MealResponse;
import com.example.meal_tracker.entity.Category;
import com.example.meal_tracker.entity.Meal;

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

    public static CategoryResponse convertToDto(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
