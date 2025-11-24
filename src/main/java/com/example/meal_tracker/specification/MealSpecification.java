package com.example.meal_tracker.specification;

import com.example.meal_tracker.entity.Category;
import com.example.meal_tracker.entity.Meal;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class MealSpecification {

    public static Specification<Meal> hasName(String name) {
        if (name == null || name.isBlank()) return null;
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Meal> hasCategoryName(String categoryName) {
        if (categoryName == null || categoryName.isBlank()) return null;

        return (root, query, cb) -> {
            // Join the categories collection
            Join<Meal, Category> join = root.join("categories", JoinType.INNER);
            return cb.equal(cb.lower(join.get("name")), categoryName.toLowerCase());
        };
    }

    public static Specification<Meal> caloriesBetween(Double minCal, Double maxCal) {
        return (root, query, builder) -> {
            if (minCal != null && maxCal != null)
                return builder.between(root.get("calories"), minCal, maxCal);
            if (minCal != null)
                return builder.greaterThanOrEqualTo(root.get("calories"), minCal);
            if (maxCal != null)
                return builder.lessThanOrEqualTo(root.get("calories"), maxCal);
            return null;
        };
    }

    public static Specification<Meal> hasIngredient(String ingredient) {
        return (root, query, builder) -> ingredient == null ? null :
                    builder.like(builder.lower(root.get("ingredient")), "%" + ingredient.toLowerCase() + "%");
    }
    
}
