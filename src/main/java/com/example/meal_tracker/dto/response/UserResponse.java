package com.example.meal_tracker.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("user_id")
    private Integer id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("birth_date")
    private LocalDate birthDate;

    @JsonProperty("height")
    private Double height;

    @JsonProperty("weight")
    private Double weight;

    @JsonProperty("bmi")
    private Double bmi;

    @JsonProperty("bmi_classification")
    private String bmiClassification;

    @JsonProperty("daily_calories")
    private Double dailyCalories;

    @JsonProperty("activity_level")
    private String activityLevel;

    @JsonProperty("goal")
    private String goal;

    @JsonProperty("created_at")
    private String createdAt;
}