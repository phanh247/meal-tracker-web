package com.example.meal_tracker.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CategoryResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private Long createdAt;
    private Long updatedAt;
}
