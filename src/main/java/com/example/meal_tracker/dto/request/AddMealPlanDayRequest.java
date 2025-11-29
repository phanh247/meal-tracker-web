package com.example.meal_tracker.dto.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;

@Data
@Builder
public class AddMealPlanDayRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public Long mealPlanId;
    public String date;

}
