package com.example.meal_tracker.service.impl;

import com.example.meal_tracker.dto.request.AddMealRequest;
import com.example.meal_tracker.dto.response.MealResponse;
import com.example.meal_tracker.entity.Category;
import com.example.meal_tracker.entity.Ingredient;
import com.example.meal_tracker.entity.Meal;
import com.example.meal_tracker.exception.NotFoundException;
import com.example.meal_tracker.repository.CategoryRepository;
import com.example.meal_tracker.repository.IngredientRepository;
import com.example.meal_tracker.repository.MealRepository;
import com.example.meal_tracker.service.ImageUploadService;
import com.example.meal_tracker.service.IngredientService;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.example.meal_tracker.common.ErrorConstant.CATEGORY_NOT_FOUND;
import static com.example.meal_tracker.common.ErrorConstant.INGREDIENT_NOT_FOUND;
import static com.example.meal_tracker.common.ErrorConstant.MEAL_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class MealServiceImpl implements MealService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MealServiceImpl.class);

    private final MealRepository mealRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final ImageUploadService imageUploadService;

    @Override
    public MealResponse addNewMeal(AddMealRequest request, MultipartFile imageFile)
    throws NotFoundException, IOException {

        // Check whether categories existed or not
        Set<Category> mealCategories = new HashSet<>();
        for (String name : request.getCategoryName()) {
            Category category = categoryRepository.findByName(name)
                    .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND + name));
            mealCategories.add(category);
        }

        // Check ingredients and calculate calories
        float totalCalories = 0;
        List<Ingredient> mealIngredients = new ArrayList<>();
        for (String ingredientName : request.getMealIngredients()) {
            Optional<Ingredient> ingredient = ingredientRepository.findByName(ingredientName);
            if (ingredient.isEmpty()) {
                LOGGER.info("Ingredient with name '{}' does not exist.", ingredientName);
                throw new NotFoundException(String.format(INGREDIENT_NOT_FOUND, ingredientName));
            }
            mealIngredients.add(ingredient.get());

            // Calculate calories
            float ingredientCalories = ingredient.get().getCalories();
            totalCalories += ingredientCalories;
        }

        // Upload image to Cloudinary
        String imageUrl = imageUploadService.upload(imageFile, imageFile.getOriginalFilename());

        Meal meal = Meal.builder()
                .name(request.getMealName())
                .description(request.getMealDescription())
                .calories(totalCalories)
                .imageUrl(imageUrl)
                .mealIngredients(mealIngredients).build()

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
    public void updateMeal(Long id, AddMealRequest request, MultipartFile imageFile) throws NotFoundException, IOException {
        // Check meal existed
        Optional<Meal> meal = checkMealExists(id);
        Meal existingMeal = meal.get();

        existingMeal.setName(request.getMealName());
        existingMeal.setDescription(request.getMealDescription());
        existingMeal.setMealInstructions(request.getMealInstructions());
        existingMeal.setCalories(request.getCalories());
        existingMeal.setDescription(request.getMealDescription());

        if (imageFile != null && !imageFile.isEmpty()) {
            String uploadedUrl = imageUploadService.upload(imageFile, imageFile.getOriginalFilename());
            existingMeal.setImageUrl(uploadedUrl); // overwrite old image
        }

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

    @Override
    public List<MealResponse> recommendSimilarMeals(Long mealId, int limit) throws NotFoundException {
        Meal meal = mealRepository.findById(mealId).orElseThrow(() -> new NotFoundException("Meal not found"));

        // Get category name
        List<String> categoryNames = meal.getCategories()
                .stream()
                .map(Category::getName)
                .toList();

        // Find similar meals to mealId
        List<Meal> similarMeals = mealRepository.findSimilarMeals(categoryNames, mealId)
                .stream()
                .limit(limit)
                .toList();

        return similarMeals.stream()
                .map(DtoConverter::convertToDto)
                .toList();
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
