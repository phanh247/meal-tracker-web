package com.example.meal_tracker.controller;

import com.example.meal_tracker.dto.request.MealRecommendationRequest;
import com.example.meal_tracker.dto.request.UserHealthInfoRequest;
import com.example.meal_tracker.dto.response.ChatbotMealRecommendationResponse;
import com.example.meal_tracker.exception.NotFoundException;
import com.example.meal_tracker.service.ChatbotRecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for AI-powered meal recommendations chatbot
 * Provides endpoints for personalized meal recommendations based on user health profile
 */
@Slf4j
@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChatbotController {
    
    private final ChatbotRecommendationService chatbotRecommendationService;
    
    /**
     * Get meal recommendations with full chatbot response
     * POST /api/chatbot/recommend
     * 
     * @param request Contains user health info and query
     * @return ChatbotMealRecommendationResponse with AI response and top 5 meals
     */
    @PostMapping(value = "/recommend", 
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMealRecommendations(@RequestBody MealRecommendationRequest request) {
        try {
            LOGGER.info("Received meal recommendation request for user");
            ChatbotMealRecommendationResponse response = chatbotRecommendationService
                    .getMealRecommendations(request);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            LOGGER.error("Error getting meal recommendations: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No meals found in database");
        } catch (Exception e) {
            LOGGER.error("Unexpected error in meal recommendation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing meal recommendation: " + e.getMessage());
        }
    }
    
    /**
     * Quick recommendation endpoint with simplified input
     * POST /api/chatbot/quick-recommend
     * 
     * @param userInfo User health information
     * @param query Natural language query from user
     * @param limit Number of recommendations (default: 5)
     * @return ChatbotMealRecommendationResponse
     */
    @PostMapping(value = "/quick-recommend",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> quickRecommend(@RequestBody UserHealthInfoRequest userInfo,
                                            @RequestParam(defaultValue = "What meals should I eat today?") String query,
                                            @RequestParam(defaultValue = "5") Integer limit) {
        try {
            LOGGER.info("Received quick recommendation request");
            ChatbotMealRecommendationResponse response = chatbotRecommendationService
                    .getAIRecommendations(userInfo, query, limit);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            LOGGER.error("Error in quick recommendation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No meals found in database");
        } catch (Exception e) {
            LOGGER.error("Unexpected error in quick recommendation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing quick recommendation: " + e.getMessage());
        }
    }
    
    /**
     * Health check endpoint
     * GET /api/chatbot/health
     */
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok(new HealthCheckResponse("Chatbot service is running"));
    }
    
    /**
     * Get recommendation criteria information
     * GET /api/chatbot/info
     */
    @GetMapping("/info")
    public ResponseEntity<?> getInfo() {
        return ResponseEntity.ok(new ChatbotInfoResponse(
                "AI-Powered Meal Recommendation Chatbot",
                "Provides personalized meal recommendations based on user health profile, dietary preferences, and fitness goals",
                new String[]{
                        "Age, Weight, Height, Gender",
                        "Activity Level and Fitness Goals",
                        "Dietary Preferences (Omnivore, Vegetarian, Vegan, Pescatarian)",
                        "Health Conditions (Diabetes, Hypertension, High Cholesterol, etc.)",
                        "Allergies and Allergen Sensitivities",
                        "Daily Calorie Requirements"
                },
                new String[]{
                        "/api/chatbot/recommend - Full recommendation with conversation",
                        "/api/chatbot/quick-recommend - Quick recommendation with user info"
                }
        ));
    }
    
    /**
     * Validate user health information
     * POST /api/chatbot/validate
     */
    @PostMapping(value = "/validate",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> validateUserInfo(@RequestBody UserHealthInfoRequest userInfo) {
        try {
            ValidationResponse response = new ValidationResponse();
            response.valid = true;
            response.bmi = userInfo.calculateBMI();
            response.bmiCategory = userInfo.getBMICategory();
            response.dailyCalories = userInfo.calculateDailyCalories();
            response.message = "User health information is valid";
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error("Error validating user info", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid user health information: " + e.getMessage());
        }
    }
    
    // Inner classes for response bodies
    
    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class HealthCheckResponse {
        private String status;
    }
    
    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class ChatbotInfoResponse {
        private String name;
        private String description;
        private String[] inputParameters;
        private String[] endpoints;
    }
    
    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class ValidationResponse {
        private boolean valid;
        private Double bmi;
        private String bmiCategory;
        private Double dailyCalories;
        private String message;
    }
    
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ChatbotController.class);
}
