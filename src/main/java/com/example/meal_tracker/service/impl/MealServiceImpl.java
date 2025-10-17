package com.example.meal_tracker.service.impl;

import com.example.meal_tracker.dto.request.AddMealRequest;
import com.example.meal_tracker.dto.response.MealResponse;
import com.example.meal_tracker.entity.Category;
import com.example.meal_tracker.entity.Meal;
import com.example.meal_tracker.exception.MealManagementException;
import com.example.meal_tracker.exception.NotFoundException;
import com.example.meal_tracker.repository.CategoryRepository;
import com.example.meal_tracker.repository.MealRepository;
import com.example.meal_tracker.service.MealService;
import com.example.meal_tracker.util.ConverterUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.meal_tracker.common.ErrorConstant.CATEGORY_NOT_FOUND;
import static com.example.meal_tracker.common.ErrorConstant.MEAL_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class MealServiceImpl implements MealService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MealServiceImpl.class);

    private final MealRepository mealRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public MealResponse addNewMeal(AddMealRequest request) throws NotFoundException {
        Optional<Category> category = categoryRepository.findByName(request.getCategoryName());
        if (category.isEmpty()) {
            LOGGER.info("Category with name '{}' does not exist.", request.getCategoryName());
            throw new NotFoundException(String.format(CATEGORY_NOT_FOUND, request.getCategoryName()));
        }
        Meal meal = ConverterUtil.convertToEntity(request);
        meal.setCategory(category.get());
        mealRepository.save(meal);

        return ConverterUtil.convertToDto(meal);
    }

    @Override
    public Page<MealResponse> getMeals(Pageable pageable) {
        pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Meal> meals = mealRepository.findAll(pageable);
        return meals.map(ConverterUtil::convertToDto);
    }

    @Override
    public MealResponse getMealById(Long id) throws NotFoundException {
        Optional<Meal> meal = checkMealExists(id);
        return ConverterUtil.convertToDto(meal.get());
    }

    @Override
    public void updateMeal(Long id, AddMealRequest request) throws NotFoundException {
        Optional<Meal> meal = checkMealExists(id);
        Meal existingMeal = meal.get();
        existingMeal.setName(request.getMealName());
        existingMeal.setImageUrl(request.getMealImageUrl());
        existingMeal.setCalories(request.getCalories());
        existingMeal.setDescription(request.getMealDescription());

        LOGGER.info("Updating meal with id: {}", id);
        mealRepository.save(existingMeal);
    }

    @Override
    public void deleteMeal(Long id) throws NotFoundException {
        Optional<Meal> meal = checkMealExists(id);
        LOGGER.info("Deleting meal with id: {}", id);
        mealRepository.delete(meal.get());
    }

    private Optional<Meal> checkMealExists(Long id) throws NotFoundException {
        Optional<Meal> meal = mealRepository.findById(id);
        if (meal.isEmpty()) {
            LOGGER.info(MEAL_NOT_FOUND, id);
            throw new NotFoundException(String.format(MEAL_NOT_FOUND, id));
        }
        return meal;
    }
}
