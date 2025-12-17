package com.example.meal_tracker.repository;

import com.example.meal_tracker.entity.MealPlan;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {
    Optional<MealPlan> findByName(String mealPlanName);

    Page<MealPlan> findByUserId(Pageable pageable, Long userId);

    @Query("""
            SELECT mp FROM MealPlan mp
            WHERE mp.userId = :userId
            AND mp.isActive = true
            """)
    Optional<MealPlan> findActiveMealPlanByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("""
                update MealPlan m
                set m.isActive = false
                where m.isActive = true
                  and m.id <> :mealPlanId
            """)
    void deactivateOtherMealPlans(@Param("mealPlanId") Long mealPlanId);

}
