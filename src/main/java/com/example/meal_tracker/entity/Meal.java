package com.example.meal_tracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "meals")
public class Meal implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "meal_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "meal_name", nullable = false)
    private String name;
    @Column(name = "meal_image_url")
    private String imageUrl;
    @Column(name = "meal_calories", nullable = false)
    private float calories;
    @Column(name = "meal_description")
    private String description;
    @Column(name = "created_at", nullable = false)
    private Long createdAt;
    @Column(name = "updated_at")
    private Long updatedAt;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
