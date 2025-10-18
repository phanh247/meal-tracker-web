package com.example.meal_tracker.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class AddCategoryRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String categoryName;
}
