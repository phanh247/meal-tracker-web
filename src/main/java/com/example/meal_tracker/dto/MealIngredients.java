package com.example.meal_tracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealIngredients {
    @JsonProperty("ingredient_id")
    private Long ingredientId;

    @JsonProperty("ingredient_name")
    private String ingredientName;

    @JsonProperty("quantity")
    private float quantity;

    @JsonProperty("unit")
    private String unit;
}
