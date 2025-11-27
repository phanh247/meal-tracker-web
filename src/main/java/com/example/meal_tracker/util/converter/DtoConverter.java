package com.example.meal_tracker.util.converter;

import com.example.meal_tracker.dto.MealIngredients;
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
import com.example.meal_tracker.entity.MealIngredient;
import com.example.meal_tracker.entity.MealPlan;
import com.example.meal_tracker.entity.MealPlanDay;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class DtoConverter {

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

        List<MealIngredients> mealIngredients = new ArrayList<>();
        for (MealIngredient mi : meal.getMealIngredients()) {
            MealIngredients ingredient = new MealIngredients();
            ingredient.setIngredientName(mi.getIngredient().getName());
            ingredient.setQuantity(mi.getQuantity());
            mealIngredients.add(ingredient);
        }

        return MealResponse.builder()
                .id(meal.getId())
                .name(meal.getName())
                .description(meal.getDescription())
                .calories(meal.getCalories())
                .imageUrl(meal.getImageUrl())
                .mealIngredients(mealIngredients)
                .mealInstructions(meal.getMealInstructions())
                .cookingTime(meal.getCookingTime())
                .servings(meal.getServings())
                .nutrition(meal.getNutrition())
                .categoryName(categoryNames)
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
