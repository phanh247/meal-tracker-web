package com.example.meal_tracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

import com.example.meal_tracker.common.MealType;
import com.example.meal_tracker.common.PlanType;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "meal_plan_item")
public class MealPlanItem implements Serializable {
   private static final long serialVersionUID = 1L;

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   // nhiều mealPlanItems thuộc 1 mealPlan
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "meal_plan_id", nullable = false)
   private MealPlan mealPlan;

   @Column(name = "meal_plan_id", nullable = false)
   private Long mealPlanId;

   // nhiều mealPlanItems có thể có cùng 1 meal
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "meal_id", nullable = false)
   private Meal meal;

   @Column(name = "meal_id", nullable = false)
   private Long mealId;

   @Enumerated(EnumType.STRING) // Lưu giá trị enum dưới dạng String
   @Column(name = "meal_type", nullable = false)
   private MealType mealType;

   @Column(name = "meal_date", nullable = false)
   private Date mealDate;

   @Column(name = "created_at", nullable = false)
   private Long createdAt;

   @Column(name = "updated_at")
   private Long updatedAt;

}
