package com.example.meal_tracker.repository;

import com.example.meal_tracker.entity.MealPlanDay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealPlanDayRepository extends JpaRepository<MealPlanDay, Long> {
}
