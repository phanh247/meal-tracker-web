package com.example.meal_tracker.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class MealResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String imageUrl;
    private float calories;
    private String description;
    private Long createdAt;
    private Long updatedAt;
}
