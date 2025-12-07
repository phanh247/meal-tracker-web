package com.example.meal_tracker.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for capturing user health and fitness information
 * Used for personalized meal recommendations
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserHealthInfoRequest {
    
    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 150, message = "Age must be less than 150")
    private Integer age;
    
    @NotNull(message = "Weight is required (in kg)")
    @DecimalMin(value = "20.0", message = "Weight must be at least 20 kg")
    @DecimalMax(value = "300.0", message = "Weight must not exceed 300 kg")
    private Double weight; // in kg
    
    @NotNull(message = "Height is required (in cm)")
    @DecimalMin(value = "100.0", message = "Height must be at least 100 cm")
    @DecimalMax(value = "250.0", message = "Height must not exceed 250 cm")
    private Double height; // in cm
    
    private String gender; // MALE, FEMALE, OTHER
    
    private String activityLevel; // SEDENTARY, LIGHTLY_ACTIVE, MODERATELY_ACTIVE, VERY_ACTIVE, EXTREMELY_ACTIVE
    
    private String fitnessGoal; // WEIGHT_LOSS, MAINTENANCE, WEIGHT_GAIN
    
    private Double dailyCalorieTarget; // Optional, auto-calculated if not provided
    
    /**
     * Calculate BMI from weight and height
     * BMI = weight (kg) / (height (m))^2
     */
    public Double calculateBMI() {
        if (weight != null && height != null && height > 0) {
            double heightInMeters = height / 100.0;
            return weight / (heightInMeters * heightInMeters);
        }
        return null;
    }
    
    /**
     * Get BMI category
     */
    public String getBMICategory() {
        Double bmi = calculateBMI();
        if (bmi == null) return "UNKNOWN";
        
        if (bmi < 18.5) return "UNDERWEIGHT";
        if (bmi < 25) return "NORMAL_WEIGHT";
        if (bmi < 30) return "OVERWEIGHT";
        return "OBESE";
    }
    
    /**
     * Calculate daily calorie needs if not provided
     * Uses Harris-Benedict equation
     */
    public Double calculateDailyCalories() {
        if (dailyCalorieTarget != null && dailyCalorieTarget > 0) {
            return dailyCalorieTarget;
        }
        
        if (weight == null || height == null || age == null || gender == null) {
            return null;
        }
        
        double bmr;
        
        // Harris-Benedict equation
        if ("MALE".equals(gender)) {
            bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
        } else {
            bmr = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
        }
        
        // Apply activity factor
        double activityFactor = getActivityFactor();
        return bmr * activityFactor;
    }
    
    private double getActivityFactor() {
        if (activityLevel == null) return 1.55; // Default to moderately active
        
        return switch (activityLevel) {
            case "SEDENTARY" -> 1.2;
            case "LIGHTLY_ACTIVE" -> 1.375;
            case "MODERATELY_ACTIVE" -> 1.55;
            case "VERY_ACTIVE" -> 1.725;
            case "EXTREMELY_ACTIVE" -> 1.9;
            default -> 1.55;
        };
    }
}
