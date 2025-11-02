package com.example.meal_tracker.repository;

import com.example.meal_tracker.entity.MealPlan;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {
    Optional<MealPlan> findByName(String mealPlanName);
}
