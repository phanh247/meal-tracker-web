package com.example.meal_tracker.dto.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class AddMealRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String mealName;
    private String mealDescription;
    private String mealImageUrl;
    private float calories;

    @Override
    public String toString() {
        return "AddMealRequest{" +
                "calories=" + calories +
                ", mealName='" + mealName + '\'' +
                ", mealDescription='" + mealDescription + '\'' +
                ", mealImageUrl='" + mealImageUrl + '\'' +
                '}';
    }
}
