package com.example.meal_tracker.dto.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

import com.example.meal_tracker.common.MealType;

@Data
@Builder
public class AddMealPlanItemRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public Long mealPlanId;
    public Long mealId;
    public MealType mealType;
    public LocalDate mealDate;

}
