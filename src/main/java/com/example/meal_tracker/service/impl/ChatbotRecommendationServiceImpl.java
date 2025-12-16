package com.example.meal_tracker.service.impl;

import com.example.meal_tracker.dto.request.ChatMessageRequest;
import com.example.meal_tracker.dto.request.MealRecommendationRequest;
import com.example.meal_tracker.dto.request.UserHealthInfoRequest;
import com.example.meal_tracker.dto.response.ChatMessageResponse;
import com.example.meal_tracker.dto.response.ChatbotMealRecommendationResponse;
import com.example.meal_tracker.dto.response.MealRecommendationResponse;
import com.example.meal_tracker.entity.Meal;
import com.example.meal_tracker.exception.NotFoundException;
import com.example.meal_tracker.repository.MealRepository;
import com.example.meal_tracker.service.ChatbotRecommendationService;
import com.example.meal_tracker.util.AIPromptBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
    
    @Value("${openai.api.url:https://api.openai.com/v1/chat/completions}")
    private String openAiUrl;
    
    private static final Gson gson = new Gson();
    
    public ChatbotRecommendationServiceImpl(MealRepository mealRepository, AIPromptBuilder promptBuilder) {
        this.mealRepository = mealRepository;
        this.promptBuilder = promptBuilder;
    }
    
    @Override
    public ChatbotMealRecommendationResponse getMealRecommendations(MealRecommendationRequest request)
            throws NotFoundException {
        
        UserHealthInfoRequest userInfo = request.getUserHealthInfo();
        
        // Get all meals from database
        List<Meal> allMeals = mealRepository.findAll();
        if (allMeals.isEmpty()) {
            throw new NotFoundException("No meals found in database");
        }
        
        // Convert to recommendation responses
        List<MealRecommendationResponse> mealResponses = allMeals.stream()
                .map(this::convertToRecommendationResponse)
                .collect(Collectors.toList());
        
        log.info("Total meals in database: {}", mealResponses.size());
        
        // Build prompt with user info and all meals
        String prompt = promptBuilder.buildTop5MealsPrompt(userInfo, mealResponses);
        log.debug("Generated prompt for AI");
        
        // Get top 5 recommendations from AI
        List<MealRecommendationResponse> topRecommendations = getTop5MealsFromAI(prompt, mealResponses);
        
        if (topRecommendations.isEmpty()) {
            throw new NotFoundException("No meal recommendations could be generated");
        }
        
        // Calculate summary statistics
        Double totalCalories = topRecommendations.stream()
                .mapToDouble(m -> m.getCalories() != null ? m.getCalories() : 0)
                .sum();
        
        String nutritionalSummary = generateNutritionalSummary(userInfo, topRecommendations);
        
        return ChatbotMealRecommendationResponse.builder()
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
    public ChatMessageResponse processChatMessage(ChatMessageRequest request) throws NotFoundException {
        String message = request.getMessage();
        log.info("Processing chat message: {}", message);
        
        // Check if message contains keywords for meal recommendation
        boolean isMealRecommendationRequest = message.toLowerCase().contains("5 món ăn") 
                                           || message.toLowerCase().contains("5 meals")
                                           || message.toLowerCase().contains("recommend")
                                           || message.toLowerCase().contains("gợi ý món");
        
        if (isMealRecommendationRequest && request.getUserHealthInfo() != null) {
            log.info("Message contains meal recommendation request, fetching recommendations");
            // Process as meal recommendation request
            ChatbotMealRecommendationResponse recResponse = getMealRecommendations(
                    MealRecommendationRequest.builder()
                            .userHealthInfo(request.getUserHealthInfo())
                            .limitResults(5)
                            .build()
            );
            
            return ChatMessageResponse.builder()
                    .response("Tôi đã tìm ra 5 món ăn tốt nhất cho bạn")
                    .responseType("MEAL_RECOMMENDATION")
                    .recommendations(recResponse.getRecommendations())
                    .totalRecommendations(recResponse.getTotalRecommendations())
                    .estimatedTotalCalories(recResponse.getEstimatedTotalCalories())
                    .nutritionalSummary(recResponse.getNutritionalSummary())
                    .timestamp(System.currentTimeMillis())
                    .build();
        } else {
            // Process as general chat request
            log.info("Message is general chat, calling AI API");
            String chatResponse = processChatResponse(message);
            
            return ChatMessageResponse.builder()
                    .response(chatResponse)
                    .responseType("CHAT")
                    .timestamp(System.currentTimeMillis())
                    .build();
        }
    }
    
    /**
     * Process general chat message with AI
     */
    private String processChatResponse(String message) {
        try {
            String prompt = promptBuilder.buildChatMessagePrompt(message);
            return callAI(prompt);
        } catch (Exception e) {
            log.error("Error processing chat message: {}", e.getMessage(), e);
            return "Xin lỗi, tôi không thể xử lý yêu cầu của bạn lúc này. Vui lòng thử lại sau.";
        }
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
                .build();
    }
    
    /**
     * Get top 5 meals from AI based on JSON response
     */
    private List<MealRecommendationResponse> getTop5MealsFromAI(String prompt, 
                                                               List<MealRecommendationResponse> availableMeals) {
        try {
            String aiResponse = callAI(prompt);
            log.info("AI Response received: {}", aiResponse.substring(0, Math.min(100, aiResponse.length())));
            
            // Parse JSON response from AI
            List<MealRecommendationResponse> recommendations = parseAIResponse(aiResponse, availableMeals);
            
            if (recommendations.isEmpty()) {
                log.warn("Failed to parse AI recommendations, using fallback");
                return getFallbackRecommendations(availableMeals, 5);
            }
            
            return recommendations.stream().limit(5).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting recommendations from AI: {}", e.getMessage(), e);
            // Return fallback recommendations
            return getFallbackRecommendations(availableMeals, 5);
        }
    }
    
    /**
     * Parse AI JSON response and match with available meals
     */
    private List<MealRecommendationResponse> parseAIResponse(String jsonResponse, 
                                                            List<MealRecommendationResponse> availableMeals) {
        try {
            // Extract JSON from response (in case AI returns extra text)
            String cleanJson = extractJsonArray(jsonResponse);
            
            JsonArray jsonArray = gson.fromJson(cleanJson, JsonArray.class);
            List<MealRecommendationResponse> recommendations = new ArrayList<>();
            
            for (int i = 0; i < jsonArray.size() && recommendations.size() < 5; i++) {
                JsonObject item = jsonArray.get(i).getAsJsonObject();
                String mealName = item.has("mealName") ? item.get("mealName").getAsString() : "";
                Double calories = item.has("calories") ? item.get("calories").getAsDouble() : null;
                Float matchScore = item.has("matchScore") ? item.get("matchScore").getAsFloat() : 0f;
                String reason = item.has("recommendationReason") ? item.get("recommendationReason").getAsString() : "";
                
                // Find matching meal in available meals
                MealRecommendationResponse matchedMeal = availableMeals.stream()
                        .filter(m -> m.getMealName().equalsIgnoreCase(mealName))
                        .findFirst()
                        .orElse(null);
                
                if (matchedMeal != null) {
                    matchedMeal.setMatchScore(matchScore);
                    matchedMeal.setRecommendationReason(reason);
                    recommendations.add(matchedMeal);
                } else {
                    // If meal not found, create a new one from AI response
                    MealRecommendationResponse newMeal = MealRecommendationResponse.builder()
                            .mealName(mealName)
                            .calories(calories)
                            .matchScore(matchScore)
                            .recommendationReason(reason)
                            .build();
                    recommendations.add(newMeal);
                }
            }
            
            log.info("Parsed {} recommendations from AI response", recommendations.size());
            return recommendations;
        } catch (Exception e) {
            log.error("Failed to parse AI JSON response: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Extract JSON array from string (handles cases where AI returns extra text)
     */
    private String extractJsonArray(String text) {
        int startIdx = text.indexOf('[');
        int endIdx = text.lastIndexOf(']');
        
        if (startIdx != -1 && endIdx != -1 && endIdx > startIdx) {
            return text.substring(startIdx, endIdx + 1);
        }
        
        return text;
    }
    
    /**
     * Get fallback recommendations using simple scoring
     */
    private List<MealRecommendationResponse> getFallbackRecommendations(List<MealRecommendationResponse> availableMeals,
                                                                       int limit) {
        log.warn("Using fallback recommendations");
        // Simply return top meals by calories that seem reasonable
        return availableMeals.stream()
                .filter(m -> m.getCalories() != null && m.getCalories() > 0 && m.getCalories() < 2000)
                .sorted(Comparator.comparingDouble(MealRecommendationResponse::getCalories).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    private String generateNutritionalSummary(UserHealthInfoRequest userInfo,
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
     * Call AI API (Hugging Face or OpenAI)
     */
    private String callAI(String prompt) throws IOException, InterruptedException {
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
        
        log.warn("No AI API key configured");
        throw new IOException("No AI API key configured");
    }
    
    /**
     * Call Hugging Face Inference API
     */
    private String callHuggingFaceAPI(String prompt) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", prompt);
        
        JsonArray messages = new JsonArray();
        messages.add(message);
        
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", huggingFaceModel);
        requestBody.add("messages", messages);
        requestBody.addProperty("max_tokens", 1000);
        requestBody.addProperty("temperature", 0.7);
        
        String apiUrl = huggingFaceBaseUrl + "/chat/completions";
        log.info("Calling Hugging Face API: {}", apiUrl);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Authorization", "Bearer " + huggingFaceApiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        log.info("Hugging Face API response status: {}", response.statusCode());
        
        if (response.statusCode() == 200) {
            JsonObject responseBody = gson.fromJson(response.body(), JsonObject.class);
            if (responseBody.has("choices") && responseBody.getAsJsonArray("choices").size() > 0) {
                JsonObject choice = responseBody.getAsJsonArray("choices").get(0).getAsJsonObject();
                if (choice.has("message")) {
                    String content = choice.getAsJsonObject("message").get("content").getAsString();
                    log.info("Successfully generated response from Hugging Face");
                    return content.trim();
                }
            }
            throw new IOException("Invalid response structure from Hugging Face");
        } else if (response.statusCode() == 503) {
            throw new IOException("Hugging Face model is loading, please try again later");
        } else {
            throw new IOException("Hugging Face API error: " + response.statusCode());
        }
    }
    
    /**
     * Call OpenAI API
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
        requestBody.addProperty("max_tokens", 1000);
        
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
}
