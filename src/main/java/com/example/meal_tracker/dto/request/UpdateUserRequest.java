package com.example.meal_tracker.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {

    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    @Pattern(regexp = "male|female|other", message = "Gender must be 'male', 'female', or 'other'")
    private String gender;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @Positive(message = "Height must be positive")
    @DecimalMax(value = "300.0", message = "Height must be less than 300 cm")
    private Double height;

    @Positive(message = "Weight must be positive")
    @DecimalMax(value = "500.0", message = "Weight must be less than 500 kg")
    private Double weight;

    @Pattern(regexp = "sedentary|light|moderate|active|very_active", message = "Activity level must be: sedentary, light, moderate, active, or very_active")
    private String activityLevel;

    @Pattern(regexp = "lose_weight|maintain|gain_weight", message = "Goal must be: lose_weight, maintain, or gain_weight")
    private String goal;
}