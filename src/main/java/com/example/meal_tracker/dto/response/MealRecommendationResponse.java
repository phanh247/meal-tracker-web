package com.example.meal_tracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for recommended meal with AI recommendation reasoning
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MealRecommendationResponse {
    
    private Long mealId;
    private String mealName;
    private String description;
    private String imageUrl;
    private Double calories;
    private String cookingTime;
    private Integer servings;
    private String category;
    private Float matchScore; // 0-100: How well this meal matches the user's profile
    private String recommendationReason; // Why this meal was recommended
    private String nutritionalBenefits; // Key nutritional benefits
    
    // Interactive link information
    private String mealLink; // URL to view full meal details
}
