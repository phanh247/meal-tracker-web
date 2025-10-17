package com.example.meal_tracker.util;

import com.example.meal_tracker.dto.request.AddMealRequest;
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
}
