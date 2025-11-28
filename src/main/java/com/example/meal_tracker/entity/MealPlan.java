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
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@Builder
//@Table(name = "meal_plan")
//public class MealPlan implements Serializable {
//    private static final long serialVersionUID = 1L;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    // @ManyToOne(fetch = FetchType.EAGER)
//    // @JoinColumn(name = "user_id", nullable = false)
//    // private User user;
//
//    @Column(name = "user_id", nullable = false)
//    private Long userId;
//
//    @Column(name = "meal_plan_name", nullable = false, unique = true)
//    private String name;
//
//    @Column(name = "is_suggested", nullable = false)
//    private Boolean isSuggested;
//
//    @Column(name = "created_at", nullable = false)
//    private Long createdAt;
//
//    @Column(name = "updated_at")
//    private Long updatedAt;
//
//}
