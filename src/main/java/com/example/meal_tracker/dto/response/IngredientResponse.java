package com.example.meal_tracker.dto.response;

import com.example.meal_tracker.entity.Ingredient;
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

    public static IngredientResponse fromEntity(Ingredient ingredient) {
        return IngredientResponse.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .calories(ingredient.getCalories())
                .build();
    }
}
