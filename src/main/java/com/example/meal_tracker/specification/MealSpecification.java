package com.example.meal_tracker.specification;

import com.example.meal_tracker.entity.Meal;
import org.springframework.data.jpa.domain.Specification;

public class MealSpecification {

    public static Specification<Meal> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Meal> hasCategoryName(String categoryName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("category").get("name")), categoryName.toLowerCase());
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
    
}
