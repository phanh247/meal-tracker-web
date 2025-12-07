package com.example.meal_tracker.util;

import com.example.meal_tracker.dto.request.UserHealthInfoRequest;
import com.example.meal_tracker.dto.response.MealRecommendationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for building AI prompts for OpenAI or other LLMs
 */
@Slf4j
@Component
public class AIPromptBuilder {
    
    /**
     * Build a comprehensive prompt for meal recommendations
     */
    public String buildRecommendationPrompt(UserHealthInfoRequest userInfo,
                                           List<MealRecommendationResponse> recommendations,
                                           String userQuery) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("You are a professional nutritionist and meal recommendation expert. ");
        prompt.append("Based on the user's health profile and the recommended meals, provide a personalized and encouraging response.\n\n");
        
        // User Profile Section
        prompt.append("=== USER HEALTH PROFILE ===\n");
        prompt.append(String.format("Age: %d years\n", userInfo.getAge()));
        prompt.append(String.format("Weight: %.1f kg\n", userInfo.getWeight()));
        prompt.append(String.format("Height: %.1f cm\n", userInfo.getHeight()));
        prompt.append(String.format("Gender: %s\n", userInfo.getGender()));
        prompt.append(String.format("BMI: %.1f (%s)\n", userInfo.calculateBMI(), userInfo.getBMICategory()));
        prompt.append(String.format("Activity Level: %s\n", userInfo.getActivityLevel()));
        prompt.append(String.format("Fitness Goal: %s\n", userInfo.getFitnessGoal()));
        prompt.append(String.format("Daily Calorie Needs: %.0f calories\n", userInfo.calculateDailyCalories()));
        
        prompt.append("\n");
        
        // Recommended Meals Section
        prompt.append("=== TOP RECOMMENDED MEALS ===\n");
        for (int i = 0; i < recommendations.size(); i++) {
            MealRecommendationResponse meal = recommendations.get(i);
            prompt.append(String.format("%d. %s\n", i + 1, meal.getMealName()));
            prompt.append(String.format("   Calories: %.0f\n", meal.getCalories()));
            if (meal.getDescription() != null) {
                prompt.append(String.format("   Description: %s\n", meal.getDescription()));
            }
            prompt.append(String.format("   Match Score: %.1f%%\n", meal.getMatchScore()));
            prompt.append("\n");
        }
        
        // User Query
        prompt.append("=== USER QUESTION/REQUEST ===\n");
        prompt.append(userQuery).append("\n\n");
        
        // Instructions
        prompt.append("=== INSTRUCTIONS ===\n");
        prompt.append("1. Provide a warm, encouraging, and personalized response\n");
        prompt.append("2. Explain why each meal is suitable for this user's fitness profile\n");
        prompt.append("3. Mention how the meals align with their fitness goals and activity level\n");
        prompt.append("4. Provide practical serving suggestions or preparation tips if relevant\n");
        prompt.append("5. Keep the response concise (2-3 paragraphs maximum)\n");
        prompt.append("6. Be professional but friendly in tone\n");
        prompt.append("7. Avoid medical advice, but can mention general nutritional benefits\n");
        
        return prompt.toString();
    }
    
    /**
     * Build a prompt for meal database analysis
     */
    public String buildMealAnalysisPrompt(UserHealthInfoRequest userInfo,
                                         List<String> availableMeals) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("Analyze the following meals and recommend the best ones for this user profile.\n\n");
        
        prompt.append("=== USER PROFILE ===\n");
        prompt.append(String.format("BMI: %.1f (%s)\n", userInfo.calculateBMI(), userInfo.getBMICategory()));
        prompt.append(String.format("Daily Calorie Needs: %.0f\n", userInfo.calculateDailyCalories()));
        prompt.append(String.format("Fitness Goal: %s\n", userInfo.getFitnessGoal()));

        prompt.append("\n=== AVAILABLE MEALS ===\n");
        for (int i = 0; i < availableMeals.size(); i++) {
            prompt.append(String.format("%d. %s\n", i + 1, availableMeals.get(i)));
        }
        
        prompt.append("\n=== TASK ===\n");
        prompt.append("Rank these meals from best to worst for the user based on:\n");
        prompt.append("1. Caloric alignment with their daily needs\n");
        prompt.append("2. Suitability for their fitness goal\n");
        prompt.append("3. Compatibility with their dietary preference\n");
        prompt.append("4. Nutritional value\n");
        prompt.append("5. Variety\n\n");
        prompt.append("Return a JSON array with meal rankings and brief explanations.");
        
        return prompt.toString();
    }
    
    /**
     * Build a prompt for nutritional analysis
     */
    public String buildNutritionalAnalysisPrompt(UserHealthInfoRequest userInfo,
                                                List<MealRecommendationResponse> meals) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("Provide a nutritional analysis for the following meal plan.\n\n");
        
        prompt.append("=== USER INFO ===\n");
        prompt.append(String.format("Age: %d\n", userInfo.getAge()));
        prompt.append(String.format("Daily Calorie Goal: %.0f\n", userInfo.calculateDailyCalories()));
        
        prompt.append("\n=== PLANNED MEALS ===\n");
        for (MealRecommendationResponse meal : meals) {
            prompt.append(String.format("- %s: %.0f calories\n", meal.getMealName(), meal.getCalories()));
        }
        
        prompt.append("\n=== ANALYSIS NEEDED ===\n");
        prompt.append("1. Is the total caloric intake appropriate?\n");
        prompt.append("2. Are there nutritional imbalances?\n");
        prompt.append("3. Suggested adjustments for better nutrition\n");
        prompt.append("4. Overall health recommendation\n");
        
        return prompt.toString();
    }
    
    /**
     * Build a simple query prompt for free-form questions
     */
    public String buildSimpleQueryPrompt(String userQuery,
                                        UserHealthInfoRequest userInfo) {
        return String.format(
                "You are a nutrition expert. A user with the following profile is asking: \"%s\"\n\n" +
                "User Profile:\n" +
                "- Age: %d\n" +
                "- BMI: %.1f\n" +
                "- Goal: %s\n" +
                "- Dietary Preference: %s\n\n" +
                "Provide a helpful, personalized response.",
                userQuery,
                userInfo.getAge(),
                userInfo.calculateBMI(),
                userInfo.getFitnessGoal()
        );
    }
}
