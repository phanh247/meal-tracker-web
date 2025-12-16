package com.example.meal_tracker.controller;

import com.example.meal_tracker.dto.request.ChatMessageRequest;
import com.example.meal_tracker.dto.request.MealRecommendationRequest;
import com.example.meal_tracker.dto.request.UserHealthInfoRequest;
import com.example.meal_tracker.dto.response.ChatMessageResponse;
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
 * Simplified to provide personalized top 5 meal recommendations based on user health profile
 */
@Slf4j
@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChatbotController {
    
    private final ChatbotRecommendationService chatbotRecommendationService;
    
    /**
     * Chat endpoint - Process user messages
     * If message contains "5 món ăn", returns meal recommendations
     * Otherwise returns general chat response
     * 
     * POST /api/chatbot/chat
     * 
     * @param request ChatMessageRequest with message and optional userHealthInfo
     * @return ChatMessageResponse with AI response
     */
    @PostMapping(value = "/chat", 
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> chatMessage(@RequestBody ChatMessageRequest request) {
        try {
            log.info("Received chat message");
            
            ChatMessageResponse response = chatbotRecommendationService
                    .processChatMessage(request);
            
            log.info("Chat response type: {}", response.getResponseType());
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            log.error("No meals found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("No meals found in database"));
        } catch (Exception e) {
            log.error("Error processing chat: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error processing message: " + e.getMessage()));
        }
    }
    
    /**
     * Get top 5 meal recommendations based on user health profile
     * POST /api/chatbot/recommend
     * 
     * Request Body: UserHealthInfoRequest with age, weight, height, gender, activityLevel, fitnessGoal
     * Response: ChatbotMealRecommendationResponse with top 5 recommended meals in JSON format
     * 
     * @param userInfo User health information
     * @return JSON with top 5 meal recommendations
     */
    @PostMapping(value = "/recommend", 
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMealRecommendations(@RequestBody UserHealthInfoRequest userInfo) {
        try {
            log.info("Received meal recommendation request");
            
            // Create recommendation request
            MealRecommendationRequest request = MealRecommendationRequest.builder()
                    .userHealthInfo(userInfo)
                    .limitResults(5)
                    .build();
            
            // Get recommendations from service
            ChatbotMealRecommendationResponse response = chatbotRecommendationService
                    .getMealRecommendations(request);
            
            log.info("Returning {} meal recommendations", response.getTotalRecommendations());
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            log.error("No meals found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("No meals found in database"));
        } catch (Exception e) {
            log.error("Error processing meal recommendation: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error processing recommendation: " + e.getMessage()));
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
    public static class ErrorResponse {
        private String error;
    }
}
