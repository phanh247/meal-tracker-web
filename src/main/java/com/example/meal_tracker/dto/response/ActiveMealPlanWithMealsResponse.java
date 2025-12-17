package com.example.meal_tracker.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.example.meal_tracker.common.PlanType;

@Data
@Builder
public class ActiveMealPlanWithMealsResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private Long userId;
    private BigDecimal targetCalories;
    private Date startDate;
    private Date endDate;
    private String note;
    private PlanType planType;
    private Boolean isActive;
    // Map với key là mealDate (YYYY-MM-DD), value là danh sách meal của ngày đó
    private Map<String, List<MealPlanItemWithMealDetailsResponse>> mealsByDate;
    private Long createdAt;
    private Long updatedAt;
}
