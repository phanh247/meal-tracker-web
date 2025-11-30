package com.example.meal_tracker.repository;

import com.example.meal_tracker.entity.MealIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MealIngredientRepository extends JpaRepository<MealIngredient, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM MealIngredient mi WHERE mi.meal.id = :mealId")
    void deleteByMealId(Long mealId);
}
