package com.example.meal_tracker.dto.request;

import lombok.Data;

@Data
public class IngredientRequest {
    private String name;
    private float calories;
    private String description;
}
