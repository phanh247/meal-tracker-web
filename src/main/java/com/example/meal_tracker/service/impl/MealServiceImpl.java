package com.example.meal_tracker.service.impl;

import com.example.meal_tracker.dto.request.AddMealRequest;
import com.example.meal_tracker.dto.response.MealResponse;
import com.example.meal_tracker.entity.Meal;
import com.example.meal_tracker.repository.MealRepository;
import com.example.meal_tracker.service.MealService;
import com.example.meal_tracker.util.ConverterUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.meal_tracker.common.ErrorConstant.MEAL_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class MealServiceImpl implements MealService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MealServiceImpl.class);

    private final MealRepository mealRepository;


    @Override
    public MealResponse addNewMeal(AddMealRequest request) {

        Meal meal = ConverterUtil.convertToEntity(request);
        mealRepository.save(meal);

        return MealResponse.builder()
                .id(meal.getId())
                .name(meal.getName())
                .imageUrl(meal.getImageUrl())
                .calories(meal.getCalories())
                .description(meal.getDescription())
                .createdAt(meal.getCreatedAt())
                .updatedAt(meal.getUpdatedAt())
                .build();
    }

    @Override
    public List<MealResponse> getMeals() {
        List<Meal> meals = mealRepository.findAll();
        if (!meals.isEmpty()) {
            return meals.stream().map(ConverterUtil::convertToDto).toList();
        } else {
            LOGGER.info("No meals found in the database.");
            return List.of();
        }
    }

    @Override
    public MealResponse getMealById(Long id) {
        return mealRepository.findById(id)
                .map(ConverterUtil::convertToDto)
                .orElseGet(() -> {
                    LOGGER.info(MEAL_NOT_FOUND, id);
                    return null;
                });
    }

    @Override
    public void updateMeal(Long id, AddMealRequest request) {

    }

    @Override
    public void deleteMeal(Long id) {

    }
}
