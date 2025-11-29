package com.example.meal_tracker.dto.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import com.example.meal_tracker.common.PlanType;

@Data
@Builder
public class UpdateMealPlanRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public String name;
    public BigDecimal targetCalories;
    public LocalDate startDate;
    public LocalDate endDate;
    public String note;
    public PlanType planType;
    public Boolean isActive;

}
