package com.example.meal_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealIngredients {
    private Long ingredientId;
    private String ingredientName;
    private float quantity;
    private String unit;
}
