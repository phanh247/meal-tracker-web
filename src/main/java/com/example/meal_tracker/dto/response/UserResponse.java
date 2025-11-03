package com.example.meal_tracker.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String gender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private Double height;
    private Double weight;
    private Double bmi;
    private Double dailyCalories;
    private String activityLevel;
    private String goal;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    // Computed fields
    private Integer age;
    private String bmiCategory;

    // Calculate age from birth date
    public Integer getAge() {
        if (birthDate == null)
            return null;
        return LocalDate.now().getYear() - birthDate.getYear();
    }

    // Get BMI category
    public String getBmiCategory() {
        if (bmi == null)
            return null;
        if (bmi < 18.5)
            return "Underweight";
        if (bmi < 25)
            return "Normal";
        if (bmi < 30)
            return "Overweight";
        return "Obese";
    }
}

// User Summary Response

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class UserSummaryResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
}