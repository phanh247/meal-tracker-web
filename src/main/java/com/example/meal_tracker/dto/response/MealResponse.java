package com.example.meal_tracker.dto.response;

import com.example.meal_tracker.dto.MealIngredients;
import com.example.meal_tracker.dto.MealInstruction;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class MealResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String description;
    private float calories;
    private String imageUrl;
    private List<MealIngredients> mealIngredients;
    private List<MealInstruction> mealInstructions;
    private String cookingTime;
    private int servings;
    private List<String> nutrition;
    private List<String> categoryName;

}
