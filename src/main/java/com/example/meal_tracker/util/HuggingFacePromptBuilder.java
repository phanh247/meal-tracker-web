package com.example.meal_tracker.util;

import com.example.meal_tracker.dto.response.MealRecommendationResponse;
import com.example.meal_tracker.dto.request.UserHealthInfoRequest;

import java.util.List;

/**
 * Utility class for generating Hugging Face API prompts
 * Uses mistral-7b-instruct model for meal recommendations
 */
public class HuggingFacePromptBuilder {
    
    /**
     * Build prompt for Hugging Face inference
     */
    public static String buildRecommendationPrompt(
            UserHealthInfoRequest userInfo,
            List<MealRecommendationResponse> recommendations,
            String userQuery) {
        
        StringBuilder prompt = new StringBuilder();
        
        // User profile section
        prompt.append("USER HEALTH PROFILE:\n");
        prompt.append("- Age: ").append(userInfo.getAge()).append(" years\n");
        prompt.append("- Weight: ").append(userInfo.getWeight()).append(" kg\n");
        prompt.append("- Height: ").append(userInfo.getHeight()).append(" cm\n");
        prompt.append("- BMI: ").append(String.format("%.1f", userInfo.calculateBMI()))
                .append(" (").append(userInfo.getBMICategory()).append(")\n");
        prompt.append("- Gender: ").append(userInfo.getGender()).append("\n");
        prompt.append("- Activity Level: ").append(userInfo.getActivityLevel()).append("\n");
        prompt.append("- Fitness Goal: ").append(userInfo.getFitnessGoal()).append("\n");
        prompt.append("- Daily Calorie Target: ").append(String.format("%.0f", userInfo.calculateDailyCalories())).append(" kcal\n");
        
        // Recommended meals section
        prompt.append("\nTOP RECOMMENDED MEALS:\n");
        if (recommendations != null && !recommendations.isEmpty()) {
            for (int i = 0; i < recommendations.size(); i++) {
                MealRecommendationResponse meal = recommendations.get(i);
                prompt.append((i + 1)).append(". ").append(meal.getMealName())
                        .append(" (").append(String.format("%.0f", meal.getCalories())).append(" kcal, ")
                        .append("Match Score: ").append(String.format("%.1f", meal.getMatchScore())).append("%)\n");
                prompt.append("   - Description: ").append(meal.getDescription()).append("\n");
                if (meal.getNutritionalBenefits() != null && !meal.getNutritionalBenefits().isEmpty()) {
                    prompt.append("   - Benefits: ").append(meal.getNutritionalBenefits()).append("\n");
                }
            }
        }
        
        // User query
        prompt.append("\nUSER QUESTION: ").append(userQuery).append("\n");
        
        // Instructions
        prompt.append("\nINSTRUCTIONS:\n");
        prompt.append("Based on the user's health profile and the recommended meals above:\n");
        prompt.append("1. Provide a warm and encouraging response\n");
        prompt.append("2. Explain why these meals are suitable for their profile\n");
        prompt.append("3. Mention how the recommendations align with their fitness goal\n");
        prompt.append("4. Provide practical tips on how to prepare or enjoy these meals\n");
        prompt.append("5. Keep response to 2-3 paragraphs, professional but friendly tone\n");
        prompt.append("6. Do NOT recommend meals not listed above\n");
        
        prompt.append("\nRESPONSE:");
        
        return prompt.toString();
    }
    
    /**
     * Build simple query prompt for free-form questions
     */
    public static String buildSimpleQueryPrompt(String query, UserHealthInfoRequest userInfo) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("You are a helpful nutrition and meal recommendation assistant.\n\n");
        prompt.append("User Profile:\n");
        prompt.append("- Goal: ").append(userInfo.getFitnessGoal()).append("\n");
        prompt.append("- Daily Calorie Target: ").append(String.format("%.0f", userInfo.calculateDailyCalories())).append(" kcal\n");

        prompt.append("\nUser Question: ").append(query).append("\n");
        prompt.append("\nProvide a helpful, practical answer in 2-3 sentences.\n");
        prompt.append("Response:");
        
        return prompt.toString();
    }
}
