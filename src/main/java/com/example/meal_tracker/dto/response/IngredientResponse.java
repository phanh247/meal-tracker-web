package com.example.meal_tracker.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IngredientResponse {
    private Long id;
    private String name;
    private float calories;
    private String description;
    private Long createdAt;
    private Long updatedAt;
}
