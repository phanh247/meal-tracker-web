package com.example.meal_tracker.entity;

import com.example.meal_tracker.dto.MealInstruction;
import com.example.meal_tracker.util.converter.MealInstructionConverter;
import com.example.meal_tracker.util.converter.StringListConverter;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "meal")
public class Meal implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "meal_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "meal_name", nullable = false)
    private String name;

    @Column(name = "meal_description")
    private String description;

    @Column(name = "meal_calories", nullable = false)
    private float calories;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "meal_instruction", columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
    private List<MealInstruction> mealInstructions;

    @Column(name = "cooking_time")
    private String cookingTime;

    @Column(name = "servings")
    private int servings;

    @Column(name = "nutrition")
    @Convert(converter = StringListConverter.class)
    private List<String> nutrition;

//    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<MealIngredient> ingredients;

    @ManyToMany
    @JoinTable(
            name = "meal_category",
            joinColumns = @JoinColumn(name = "meal_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "user_id", nullable = false)
//    private Author author;
}
