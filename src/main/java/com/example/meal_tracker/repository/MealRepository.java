package com.example.meal_tracker.repository;

import com.example.meal_tracker.dto.response.MealResponse;
import com.example.meal_tracker.entity.Meal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    Page<MealResponse> findAll(Specification<Meal> spec, Pageable pageable);
}
