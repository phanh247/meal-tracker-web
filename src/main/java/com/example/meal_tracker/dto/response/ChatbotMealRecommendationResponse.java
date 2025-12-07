package com.example.meal_tracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for chatbot meal recommendation response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatbotMealRecommendationResponse {
    
    private String aiResponse; // Natural language response from AI
    private List<MealRecommendationResponse> recommendations; // Top 5 meals
    private Integer totalRecommendations;
    private Double estimatedTotalCalories; // Sum of all recommended meals' calories
    private String nutritionalSummary; // Summary of nutritional value
    private Long timestamp;
}
