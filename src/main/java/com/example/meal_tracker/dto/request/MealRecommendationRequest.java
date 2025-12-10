package com.example.meal_tracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for chatbot meal recommendation query
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MealRecommendationRequest {
    
    @NotBlank(message = "User health info is required")
    private UserHealthInfoRequest userHealthInfo;
    
    @NotBlank(message = "Query cannot be blank")
    private String query; // e.g., "What should I eat for lunch today?"
    
    private Integer limitResults; // Number of recommendations to return (default: 5)
    
    private String mealType; // Optional: BREAKFAST, LUNCH, DINNER, SNACK
    
    private Integer maxCaloriesPerMeal; // Optional: Maximum calories per meal
    
    private String additionalContext; // Optional: Any additional information
}
