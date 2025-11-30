package com.example.meal_tracker.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.meal_tracker.common.PlanType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddMealPlanRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public String name;
    public Long userId;
    public BigDecimal targetCalories;
    public LocalDate startDate;
    public LocalDate endDate;
    public String note;
    public PlanType planType;
    public Boolean isActive;
}
