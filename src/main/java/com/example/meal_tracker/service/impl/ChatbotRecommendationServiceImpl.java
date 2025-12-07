package com.example.meal_tracker.service.impl;

import com.example.meal_tracker.dto.request.MealRecommendationRequest;
import com.example.meal_tracker.dto.request.UserHealthInfoRequest;
import com.example.meal_tracker.dto.response.ChatbotMealRecommendationResponse;
import com.example.meal_tracker.dto.response.MealRecommendationResponse;
import com.example.meal_tracker.entity.Meal;
import com.example.meal_tracker.exception.NotFoundException;
import com.example.meal_tracker.repository.MealRepository;
import com.example.meal_tracker.service.ChatbotRecommendationService;
import com.example.meal_tracker.util.AIPromptBuilder;
import com.example.meal_tracker.util.HuggingFacePromptBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatbotRecommendationServiceImpl implements ChatbotRecommendationService {
    
    private final MealRepository mealRepository;
    private final AIPromptBuilder promptBuilder;
    
    @Value("${huggingface.api.key:}")
    private String huggingFaceApiKey;
    
    @Value("${huggingface.api.base-url:https://router.huggingface.co/v1}")
    private String huggingFaceBaseUrl;
    
    @Value("${huggingface.api.model:deepseek-ai/DeepSeek-V3.2:novita}")
    private String huggingFaceModel;
    
    @Value("${openai.api.key:}")
    private String openAiApiKey;
    
    @Value("${openai.api.model:gpt-3.5-turbo}")
    private String openAiModel;
    
    public ChatbotRecommendationServiceImpl(MealRepository mealRepository, AIPromptBuilder promptBuilder) {
        this.mealRepository = mealRepository;
        this.promptBuilder = promptBuilder;
    }
    
    @Value("${openai.api.url:https://api.openai.com/v1/chat/completions}")
    private String openAiUrl;
    
    private static final Gson gson = new Gson();
    
    @Override
    public ChatbotMealRecommendationResponse getMealRecommendations(MealRecommendationRequest request)
            throws NotFoundException {
        
        UserHealthInfoRequest userInfo = request.getUserHealthInfo();
        Integer limit = request.getLimitResults() != null ? request.getLimitResults() : 5;
        
        // Get all meals from database
        List<Meal> allMeals = mealRepository.findAll();
        if (allMeals.isEmpty()) {
            throw new NotFoundException("No meals found in database");
        }
        
        // Convert to recommendation responses
        List<MealRecommendationResponse> mealResponses = allMeals.stream()
                .map(this::convertToRecommendationResponse)
                .collect(Collectors.toList());
        
        // Score meals based on user profile
        List<MealRecommendationResponse> scoredMeals = scoreMeals(mealResponses, userInfo);
        
        // Get top recommendations
        List<MealRecommendationResponse> topRecommendations = scoredMeals.stream()
                .limit(limit)
                .collect(Collectors.toList());
        
        // Generate AI response
        String aiResponse = generateAIResponse(userInfo, topRecommendations, request.getQuery());
        
        // Calculate summary statistics
        Double totalCalories = topRecommendations.stream()
                .mapToDouble(m -> m.getCalories() != null ? m.getCalories() : 0)
                .sum();
        
        String nutritionalSummary = generateNutritionalSummary(userInfo, topRecommendations);
        
        return ChatbotMealRecommendationResponse.builder()
                .aiResponse(aiResponse)
                .recommendations(topRecommendations)
                .totalRecommendations(topRecommendations.size())
                .estimatedTotalCalories(totalCalories)
                .nutritionalSummary(nutritionalSummary)
                .timestamp(System.currentTimeMillis())
                .build();
    }
    
    @Override
    public ChatbotMealRecommendationResponse getAIRecommendations(UserHealthInfoRequest userInfo,
                                                                  String query,
                                                                  Integer limit) throws NotFoundException {
        MealRecommendationRequest request = MealRecommendationRequest.builder()
                .userHealthInfo(userInfo)
                .query(query)
                .limitResults(limit != null ? limit : 5)
                .build();
        
        return getMealRecommendations(request);
    }
    
    @Override
    public List<MealRecommendationResponse> scoreMeals(List<MealRecommendationResponse> meals,
                                                       UserHealthInfoRequest userInfo) {
        return meals.stream()
                .peek(meal -> calculateMealScore(meal, userInfo))
                .sorted(Comparator.comparing(MealRecommendationResponse::getMatchScore).reversed())
                .collect(Collectors.toList());
    }
    
    /**
     * Calculate match score for a meal based on user profile
     * Score factors:
     * - Calorie alignment (70%)
     * - Fitness goal suitability (30%)
     */
    private void calculateMealScore(MealRecommendationResponse meal, UserHealthInfoRequest userInfo) {
        float score = 0f;
        
        // Calorie score (70%) - main factor
        Double dailyCalories = userInfo.calculateDailyCalories();
        if (dailyCalories != null && meal.getCalories() != null) {
            // Target meal size = daily calories / 3 (assuming 3 meals)
            double targetMealCalories = dailyCalories / 3;
            double calorieDeviation = Math.abs(meal.getCalories() - targetMealCalories) / targetMealCalories;
            float calorieScore = Math.max(0, (float) (100 - (calorieDeviation * 100)));
            score += calorieScore * 0.7f;
        } else {
            score += 50 * 0.7f; // Default half score
        }
        
        // Fitness goal score (30%)
        float goalScore = calculateGoalScore(meal, userInfo);
        score += goalScore * 0.3f;
        
        meal.setMatchScore((float) Math.min(100, score));
    }
    
    /**
     * Calculate fitness goal suitability score
     * WEIGHT_LOSS: prefer lower calorie meals
     * MAINTENANCE: neutral score
     * WEIGHT_GAIN: prefer higher calorie meals
     */
    private float calculateGoalScore(MealRecommendationResponse meal, UserHealthInfoRequest userInfo) {
        if (userInfo.getFitnessGoal() == null || meal.getCalories() == null) {
            return 50f;
        }
        
        Double dailyCalories = userInfo.calculateDailyCalories();
        if (dailyCalories == null) return 50f;
        
        double targetMealCalories = dailyCalories / 3;
        double calorieRatio = meal.getCalories() / targetMealCalories;
        
        return switch (userInfo.getFitnessGoal()) {
            case "WEIGHT_LOSS" -> {
                // Prefer meals below target (ratio < 1.0)
                if (calorieRatio < 0.8) yield 100f; // Low calorie - excellent
                if (calorieRatio < 1.0) yield 80f;  // Below target - good
                if (calorieRatio < 1.2) yield 50f;  // Slightly above - ok
                yield 20f; // High calorie - poor
            }
            case "MAINTENANCE" -> {
                // Prefer meals near target (ratio ~1.0)
                if (Math.abs(calorieRatio - 1.0) < 0.2) yield 100f; // Near target
                yield 70f; // Slightly off
            }
            case "WEIGHT_GAIN" -> {
                // Prefer meals above target (ratio > 1.0)
                if (calorieRatio > 1.2) yield 100f; // High calorie - excellent
                if (calorieRatio > 1.0) yield 80f;  // Above target - good
                if (calorieRatio > 0.8) yield 50f;  // Slightly below - ok
                yield 20f; // Low calorie - poor
            }
            default -> 50f;
        };
    }
    
    
    private MealRecommendationResponse convertToRecommendationResponse(Meal meal) {
        return MealRecommendationResponse.builder()
                .mealId(meal.getId())
                .mealName(meal.getName())
                .description(meal.getDescription())
                .imageUrl(meal.getImageUrl())
                .calories((double) meal.getCalories())
                .cookingTime(meal.getCookingTime())
                .servings(meal.getServings())
                .mealLink("/api/meal/" + meal.getId())
                .matchScore(0f)
                .recommendationReason("Recommended based on your fitness goal")
                .nutritionalBenefits(meal.getDescription() != null ? meal.getDescription() : "Nutritious meal")
                .mealLink("/api/meal/" + meal.getId())
                .build();
    }
    private String generateAIResponse(UserHealthInfoRequest userInfo,
                                      List<MealRecommendationResponse> recommendations,
                                      String query) {
        try {
            String prompt = promptBuilder.buildRecommendationPrompt(userInfo, recommendations, query);
            return callOpenAI(prompt);
        } catch (Exception e) {
            log.error("Failed to generate AI response", e);
            // Fallback response
            return String.format(
                    "Based on your profile (Age: %d, BMI: %.1f, Goal: %s, Activity: %s), I've selected the best %d meals for you. " +
                    "Each recommendation considers your calorie needs of approximately %.0f calories per day and your fitness goals. Click on any meal to see full details and nutritional information.",
                    userInfo.getAge(),
                    userInfo.calculateBMI(),
                    userInfo.getFitnessGoal(),
                    userInfo.getActivityLevel(),
                    recommendations.size(),
                    userInfo.calculateDailyCalories()
            );
        }
    } private String generateNutritionalSummary(UserHealthInfoRequest userInfo,
                                              List<MealRecommendationResponse> recommendations) {
        Double totalCalories = recommendations.stream()
                .mapToDouble(m -> m.getCalories() != null ? m.getCalories() : 0)
                .sum();
        
        Double avgCalories = recommendations.isEmpty() ? 0 : totalCalories / recommendations.size();
        Double dailyCalories = userInfo.calculateDailyCalories() != null ? userInfo.calculateDailyCalories() : 2000;
        
        return String.format(
                "Total estimated calories: %.0f | Average per meal: %.0f | Daily target: %.0f | Coverage: %.1f%%",
                totalCalories,
                avgCalories,
                dailyCalories,
                (totalCalories / dailyCalories) * 100
        );
    }

    /**
     * Call Hugging Face API or fallback to OpenAI if configured
     * If neither API key is configured, returns a default message
     */
    private String callOpenAI(String prompt) {
        // Try Hugging Face first (free)
        if (huggingFaceApiKey != null && !huggingFaceApiKey.isEmpty()) {
            try {
                return callHuggingFaceAPI(prompt);
            } catch (Exception e) {
                log.warn("Hugging Face API failed, trying OpenAI", e);
            }
        }
        
        // Fallback to OpenAI if configured
        if (openAiApiKey != null && !openAiApiKey.isEmpty()) {
            try {
                return callOpenAIAPI(prompt);
            } catch (Exception e) {
                log.warn("OpenAI API failed", e);
            }
        }
        
        log.warn("No AI API key configured, using default response");
        return getDefaultAIResponse();
    }
    
    /**
     * Call Hugging Face Inference API Provider (OpenAI-compatible endpoint)
     */
    private String callHuggingFaceAPI(String prompt) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        
        // Build OpenAI-compatible request for Hugging Face Inference API
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", prompt);
        
        JsonArray messages = new JsonArray();
        messages.add(message);
        
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", huggingFaceModel);
        requestBody.add("messages", messages);
        requestBody.addProperty("max_tokens", 500);
        requestBody.addProperty("temperature", 0.7);
        
        String apiUrl = huggingFaceBaseUrl + "/chat/completions";
        log.info("Calling Hugging Face Inference API: {}", apiUrl);
        log.debug("Using model: {}", huggingFaceModel);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Authorization", "Bearer " + huggingFaceApiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        log.info("Hugging Face API response status: {}", response.statusCode());
        log.debug("Response body: {}", response.body());
        
        if (response.statusCode() == 200) {
            JsonObject responseBody = gson.fromJson(response.body(), JsonObject.class);
            if (responseBody.has("choices") && responseBody.getAsJsonArray("choices").size() > 0) {
                JsonObject choice = responseBody.getAsJsonArray("choices").get(0).getAsJsonObject();
                if (choice.has("message")) {
                    String content = choice.getAsJsonObject("message").get("content").getAsString();
                    log.info("Successfully generated AI response from Hugging Face");
                    return content.trim();
                }
            }
            log.warn("No valid response structure from Hugging Face");
            return getDefaultAIResponse();
        } else if (response.statusCode() == 503) {
            log.warn("Hugging Face model is loading, please try again in a moment");
            throw new IOException("Hugging Face model is loading, please try again in a moment");
        } else {
            log.error("Hugging Face API error status {}: {}", response.statusCode(), response.body());
            throw new IOException("Hugging Face API error: " + response.statusCode() + " - " + response.body());
        }
    }
    
    /**
     * Call OpenAI API (fallback option)
     */
    private String callOpenAIAPI(String prompt) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", prompt);
        
        JsonArray messages = new JsonArray();
        messages.add(message);
        
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", openAiModel);
        requestBody.add("messages", messages);
        requestBody.addProperty("temperature", 0.7);
        requestBody.addProperty("max_tokens", 500);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(openAiUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + openAiApiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            JsonObject responseBody = gson.fromJson(response.body(), JsonObject.class);
            return responseBody.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content")
                    .getAsString();
        } else {
            throw new IOException("OpenAI API error: " + response.statusCode());
        }
    }
    
    private String getDefaultAIResponse() {
        return "Based on your fitness profile, I've selected the best meals for you. " +
               "Each recommendation considers your calorie needs, fitness goals, and activity level. " +
               "Click on any meal to see full details and nutritional information.";
    }
}
