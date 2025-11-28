package com.example.meal_tracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealInstruction {
    @JsonProperty("step")
    private int step;

    @JsonProperty("instruction")
    private String instruction;

    @Override
    public String toString() {
        return "MealInstruction{" +
                "instruction='" + instruction + '\'' +
                ", step=" + step +
                '}';
    }
}
