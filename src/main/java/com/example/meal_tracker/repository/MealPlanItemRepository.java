package com.example.meal_tracker.repository;

import com.example.meal_tracker.entity.MealPlanItem;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MealPlanItemRepository extends JpaRepository<MealPlanItem, Long> {
    Page<MealPlanItem> findByMealPlanId(Pageable pageable, Long mealPlanId);

    @Query("""
            SELECT mpi FROM MealPlanItem mpi
            WHERE mpi.mealPlan.id = :id
            AND mpi.mealDate = :date
            """)
    Page<MealPlanItem> findByMealPlanIdAndDate(
            @Param("id") Long id,
            @Param("date") Date date,
            Pageable pageable);

    Boolean existsByMealPlanId(Long mealPlanId);

    @Query("""
            SELECT mpi FROM MealPlanItem mpi
            WHERE mpi.mealPlan.isActive = true
            AND mpi.mealPlan.userId = :userId
            ORDER BY mpi.mealDate ASC, mpi.mealType ASC
            """)
    List<MealPlanItem> findAllByActiveMealPlanAndUserId(@Param("userId") Long userId);

}
