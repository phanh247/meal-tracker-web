package com.example.meal_tracker.repository;

import com.example.meal_tracker.entity.Meal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    Page<Meal> findAll(Specification<Meal> spec, Pageable pageable);

    //Select all meals (m) that have at least one category in :categoryNames exclude the meal with ID :mealId.
    @Query("SELECT m FROM Meal m JOIN m.categories c WHERE c.name IN :categoryNames AND m.id <> :mealId")
    List<Meal> findSimilarMeals(@Param("categoryNames") List<String> categoryNames,
                                @Param("mealId") Long mealId);
}
