package com.example.meal_tracker.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

import com.example.meal_tracker.common.MealType;

@Data
@Builder
public class MealPlanItemResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    public Long mealPlanId;
    public Long mealId;
    public MealType mealType;
    public LocalDate mealDate;
    private Long createdAt;
    private Long updatedAt;

}
