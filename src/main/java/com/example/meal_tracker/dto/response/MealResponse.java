package com.example.meal_tracker.dto.response;

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

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("meal_name")
    private String name;

    @JsonProperty("meal_description")
    private String description;

    @JsonProperty("calories")
    private float calories;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("meal_instructions")
    private List<MealInstruction> mealInstructions;

    @JsonProperty("cooking_time")
    private String cookingTime;

    @JsonProperty("servings")
    private int servings;

    @JsonProperty("nutrition")
    private List<String> nutrition;

    @JsonProperty("category_name")
    private List<String> categoryName;

}
