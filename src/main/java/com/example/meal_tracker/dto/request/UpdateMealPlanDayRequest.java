package com.example.meal_tracker.dto.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class UpdateMealPlanDayRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public Long mealPlanId;
}
