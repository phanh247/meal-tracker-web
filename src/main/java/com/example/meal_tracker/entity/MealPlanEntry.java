//package com.example.meal_tracker.entity;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.io.Serializable;
//
//import com.example.meal_tracker.common.MealType;
//
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@Builder
//@Table(name = "meal_plan_entry")
//public class MealPlanEntry implements Serializable {
//    private static final long serialVersionUID = 1L;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "meal_plan_day_id", nullable = false)
//    private MealPlanDay mealPlanDay;
//
//    @Column(name = "meal_type", nullable = false)
//    private MealType mealType;
//
//    @Column(name = "created_at", nullable = false)
//    private Long createdAt;
//
//    @Column(name = "updated_at")
//    private Long updatedAt;
//
//}
