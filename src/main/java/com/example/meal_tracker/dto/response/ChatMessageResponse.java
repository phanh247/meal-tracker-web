package com.example.meal_tracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for chat message response
 * Contains AI response which can be either:
 * 1. Top 5 meal recommendations (if message contains "5 món ăn")
 * 2. General chat response (for other questions)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageResponse {
    
    private String response; // AI response text
    private String responseType; // "MEAL_RECOMMENDATION" or "CHAT"
    private List<MealRecommendationResponse> recommendations; // Only if responseType is MEAL_RECOMMENDATION
    private Integer totalRecommendations; // Number of recommendations
    private Double estimatedTotalCalories; // Sum of calories (for recommendations)
    private String nutritionalSummary; // Nutritional info (for recommendations)
    private Long timestamp;
}
