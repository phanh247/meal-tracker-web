package com.example.meal_tracker.dto.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class UpdateMealPlanRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String mealPlanName;
    private Boolean isSuggested;
    
}
