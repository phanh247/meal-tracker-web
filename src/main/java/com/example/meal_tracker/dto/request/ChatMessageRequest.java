package com.example.meal_tracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for chat message requests
 * User can send a message and optionally provide health info
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageRequest {
    
    @NotBlank(message = "Message is required")
    private String message;
    
    // Optional: User health info (only needed if message contains "5 món ăn" or similar)
    private UserHealthInfoRequest userHealthInfo;
}
