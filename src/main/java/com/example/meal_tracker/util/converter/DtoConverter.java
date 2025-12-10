package com.example.meal_tracker.util.converter;

import com.example.meal_tracker.dto.MealIngredients;
import com.example.meal_tracker.dto.request.AddMealPlanItemRequest;
import com.example.meal_tracker.dto.request.AddMealPlanRequest;
import com.example.meal_tracker.dto.response.CategoryResponse;
import com.example.meal_tracker.dto.response.MealPlanItemResponse;
import com.example.meal_tracker.dto.response.MealPlanResponse;
import com.example.meal_tracker.dto.response.MealResponse;
import com.example.meal_tracker.entity.Category;
import com.example.meal_tracker.entity.Meal;
import com.example.meal_tracker.entity.MealIngredient;
import com.example.meal_tracker.entity.MealPlan;
import com.example.meal_tracker.entity.MealPlanItem;

import java.sql.Date;
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

        List<MealIngredients> mealIngredients = getMealIngredients(meal);

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

    private static List<MealIngredients> getMealIngredients(Meal meal) {
        List<MealIngredients> mealIngredients = new ArrayList<>();
        for (MealIngredient mi : meal.getMealIngredients()) {
            MealIngredients ingredient = new MealIngredients();
            ingredient.setIngredientId(mi.getIngredient().getId());
            ingredient.setIngredientName(mi.getIngredient().getName());
            ingredient.setQuantity(mi.getQuantity());
            ingredient.setUnit(mi.getIngredient().getDescription());
            mealIngredients.add(ingredient);
        }
        return mealIngredients;
    }

//    public static MealPlanDayResponse convertToDto(MealPlanDay mealPlanDay) {
//        return MealPlanDayResponse.builder()
//                .mealPlanId(mealPlanDay.getMealPlanId())
//                .date(mealPlanDay.getDate().toString())
//                .build();
//    }

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
                .name(request.getName())
                .userId(request.getUserId())
                .note(request.getNote())
                .startDate(Date.valueOf(request.getStartDate())) // format lại vì db đang lưu là Date
                .endDate(Date.valueOf(request.getEndDate()))
                .planType(request.getPlanType())
                .isActive(request.getIsActive())
                .targetCalories(request.getTargetCalories())
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public static MealPlanResponse convertToDto(MealPlan mealPlan) {
        return MealPlanResponse.builder()
                .id(mealPlan.getId())
                .name(mealPlan.getName())
                .userId(mealPlan.getUserId())
                .note(mealPlan.getNote())
                .startDate(mealPlan.getStartDate().toLocalDate())
                .endDate(mealPlan.getEndDate().toLocalDate())
                .planType(mealPlan.getPlanType())
                .isActive(mealPlan.getIsActive())
                .targetCalories(mealPlan.getTargetCalories())
                .createdAt(mealPlan.getCreatedAt())
                .updatedAt(mealPlan.getUpdatedAt())
                .build();
    }

    public static MealPlanItem convertToEntity(AddMealPlanItemRequest request) {
        long now = System.currentTimeMillis();
        return MealPlanItem.builder()
                .mealPlanId(request.getMealPlanId())
                .mealId(request.getMealId())
                .mealType(request.getMealType())
                .mealDate(Date.valueOf(request.getMealDate()))
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public static MealPlanItemResponse convertToDto(MealPlanItem mealPlanItem) {
        return MealPlanItemResponse.builder()
                .id(mealPlanItem.getId())
                .mealPlanId(mealPlanItem.getMealPlanId())
                .mealId(mealPlanItem.getMealId())
                .mealType(mealPlanItem.getMealType())
                .mealDate(mealPlanItem.getMealDate().toLocalDate())
                .createdAt(mealPlanItem.getCreatedAt())
                .updatedAt(mealPlanItem.getUpdatedAt())
                .build();
    }
}
