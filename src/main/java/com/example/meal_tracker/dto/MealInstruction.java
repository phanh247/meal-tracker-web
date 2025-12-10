package com.example.meal_tracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealInstruction implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int step;
    private String instruction;

    @Override
    public String toString() {
        return "MealInstruction{" +
                "instruction='" + instruction + '\'' +
                ", step=" + step +
                '}';
    }
}
