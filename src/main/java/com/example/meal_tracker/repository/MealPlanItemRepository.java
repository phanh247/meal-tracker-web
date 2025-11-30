package com.example.meal_tracker.repository;

import com.example.meal_tracker.entity.MealPlanItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealPlanItemRepository extends JpaRepository<MealPlanItem, Long> {
    Page<MealPlanItem> findByMealPlanId(Pageable pageable, Long mealPlanId);

}
