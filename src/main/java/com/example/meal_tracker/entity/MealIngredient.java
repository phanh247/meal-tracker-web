package com.example.meal_tracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meal_ingredient")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meal_id", nullable = false)
    @ToString.Exclude
    private Meal meal;

    @ManyToOne
    @JoinColumn(name = "ingredient_id", nullable = false)
    @ToString.Exclude
    private Ingredient ingredient;

    // ví dụ: grams, pieces, ml, tablespoon...
    @Column(name = "quantity", nullable = false)
    private float quantity;
}
