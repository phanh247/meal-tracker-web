package com.example.meal_tracker.dto.response;

import com.example.meal_tracker.common.MealType;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Data
@Builder
public class MealPlanItemWithMealDetailsResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long mealPlanItemId;
    private Long mealId;
    private String mealName;
    private float calories;
    private String mealDescription;
    private String imageUrl;
    private String cookingTime;
    private int servings;
    private MealType mealType;
    private Date mealDate;
    private List<String> categoryNames;
}
