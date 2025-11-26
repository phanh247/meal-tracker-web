package com.example.meal_tracker.service.impl;

import com.example.meal_tracker.dto.request.AddMealRequest;
import com.example.meal_tracker.dto.response.MealResponse;
import com.example.meal_tracker.entity.Category;
import com.example.meal_tracker.entity.Meal;
import com.example.meal_tracker.exception.NotFoundException;
import com.example.meal_tracker.repository.CategoryRepository;
import com.example.meal_tracker.repository.MealRepository;
import com.example.meal_tracker.service.ImageUploadService;
import com.example.meal_tracker.service.MealService;
import com.example.meal_tracker.specification.MealSpecification;
import com.example.meal_tracker.util.converter.DtoConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.example.meal_tracker.common.ErrorConstant.CATEGORY_NOT_FOUND;
import static com.example.meal_tracker.common.ErrorConstant.MEAL_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class MealServiceImpl implements MealService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MealServiceImpl.class);

    private final MealRepository mealRepository;
    private final CategoryRepository categoryRepository;
    private final ImageUploadService imageUploadService;

    @Override
    public MealResponse addNewMeal(AddMealRequest request, MultipartFile imageFile)
    throws NotFoundException, IOException {
        // Check whether categories existed or not
        for (String category : request.getCategoryName()) {
            if (categoryRepository.findByName(category).isEmpty()) {
                LOGGER.info("Category with name '{}' does not exist.", request.getCategoryName());
                throw new NotFoundException(String.format(CATEGORY_NOT_FOUND, request.getCategoryName()));
            }
        }

        // Upload image to Cloudinary
        String imageUrl = imageUploadService.upload(imageFile);

        Meal meal = DtoConverter.convertToEntity(request);
        meal.setImageUrl(imageUrl);
        Set<Category> mealCategories = new HashSet<>();
        for (String name : request.getCategoryName()) {
            Category category = categoryRepository.findByName(name)
                    .orElseThrow(() -> new NotFoundException("Category not found: " + name));
            mealCategories.add(category);
        }
        meal.setCategories(mealCategories);
        mealRepository.save(meal);

        return DtoConverter.convertToDto(meal);
    }

    @Override
    public Page<MealResponse> getMeals(Pageable pageable) {
        pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Meal> meals = mealRepository.findAll(pageable);
        return meals.map(DtoConverter::convertToDto);
    }

    @Override
    public MealResponse getMealById(Long id) throws NotFoundException {
        Optional<Meal> meal = checkMealExists(id);
        return DtoConverter.convertToDto(meal.get());
    }

    @Override
    public void updateMeal(Long id, AddMealRequest request) throws NotFoundException {
        Optional<Meal> meal = checkMealExists(id);
        Meal existingMeal = meal.get();
        existingMeal.setName(request.getMealName());
//        existingMeal.setImageUrl(request.getMealImageUrl());
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

    @Override
    public Page<MealResponse> filterMeals(String categoryName, String mealName, Double minCalories, Double maxCalories,
                                          String ingredientName, Pageable pageable) {
        Specification<Meal> spec = Specification
                .where(MealSpecification.hasName(mealName))
                .or(MealSpecification.hasCategoryName(categoryName))
                .or(MealSpecification.hasIngredient(ingredientName))
                .or(MealSpecification.caloriesBetween(minCalories, maxCalories));

        Page<Meal> result = mealRepository.findAll(spec, pageable);

        // Convert each Meal to MealResponse
        return result.map(DtoConverter::convertToDto);
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
