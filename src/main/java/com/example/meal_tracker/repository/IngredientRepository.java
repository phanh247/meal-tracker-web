package com.example.meal_tracker.repository;

import com.example.meal_tracker.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Optional<Ingredient> findByName(String ingredientName);

    List<Ingredient> findByNameContainingIgnoreCase(String name);
}