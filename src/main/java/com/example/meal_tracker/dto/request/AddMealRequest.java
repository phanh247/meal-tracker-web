package com.example.meal_tracker.dto.request;

import com.example.meal_tracker.dto.MealIngredients;
import com.example.meal_tracker.dto.MealInstruction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddMealRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String mealName;
    private String mealDescription;
    private List<MealIngredients> mealIngredients;
    private List<MealInstruction> mealInstructions;
    private String cookingTime;
    private int servings;
    private List<String> nutrition;
    private List<Integer> categories;
    private MultipartFile image;

    @Override
    public String toString() {
        return "AddMealRequest{" +
                "categories=" + categories +
                ", mealName='" + mealName + '\'' +
                ", mealDescription='" + mealDescription + '\'' +
                ", mealIngredients=" + mealIngredients +
                ", mealInstructions=" + mealInstructions +
                ", cookingTime='" + cookingTime + '\'' +
                ", servings=" + servings +
                ", nutrition=" + nutrition +
                ", image=" + image +
                '}';
    }
}
