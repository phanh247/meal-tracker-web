package com.example.meal_tracker.service;

import com.example.meal_tracker.dto.request.MealRecommendationRequest;
import com.example.meal_tracker.dto.request.UserHealthInfoRequest;
import com.example.meal_tracker.dto.response.ChatbotMealRecommendationResponse;
import com.example.meal_tracker.dto.response.MealRecommendationResponse;
import com.example.meal_tracker.exception.NotFoundException;

import java.util.List;

public interface ChatbotRecommendationService {
    
    /**
     * Get personalized meal recommendations based on user health info
     */
    ChatbotMealRecommendationResponse getMealRecommendations(MealRecommendationRequest request) 
            throws NotFoundException;
    
    /**
     * Get meal recommendations with AI conversational response
     */
    ChatbotMealRecommendationResponse getAIRecommendations(UserHealthInfoRequest userInfo, 
                                                           String query,
                                                           Integer limit) throws NotFoundException;
    
    /**
     * Score and rank meals based on user profile
     */
    List<MealRecommendationResponse> scoreMeals(List<MealRecommendationResponse> meals, 
                                               UserHealthInfoRequest userInfo);
}
