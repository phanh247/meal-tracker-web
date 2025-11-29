package com.example.meal_tracker.dto.request;

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
public class UpdateUserRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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

    @JsonProperty("activity_level")
    private String activityLevel;

    @JsonProperty("goal")
    private String goal;
}