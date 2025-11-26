package com.example.meal_tracker.dto.request;

import com.example.meal_tracker.dto.MealInstruction;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class AddMealRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("meal_name")
    private String mealName;

    @JsonProperty("meal_description")
    private String mealDescription;

    @JsonProperty("calories")
    private float calories;

    @JsonProperty("meal_instruction")
    private List<MealInstruction> mealInstructions;

    @JsonProperty("cooking_time")
    private String cookingTime;

    @JsonProperty("servings")
    private int servings;

    @JsonProperty("nutrition")
    private List<String> nutrition;

    @JsonProperty("category_name")
    private List<String> categoryName;

    @Override
    public String toString() {
        return "AddMealRequest{" +
                "calories=" + calories +
                ", mealName='" + mealName + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", mealDescription='" + mealDescription + '\'' +
                '}';
    }
}
