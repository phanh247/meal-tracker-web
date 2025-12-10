package com.example.meal_tracker.service.impl;

import com.example.meal_tracker.dto.MealIngredients;
import com.example.meal_tracker.dto.request.AddMealRequest;
import com.example.meal_tracker.dto.response.MealResponse;
import com.example.meal_tracker.entity.Category;
import com.example.meal_tracker.entity.Ingredient;
import com.example.meal_tracker.entity.Meal;
import com.example.meal_tracker.entity.MealIngredient;
import com.example.meal_tracker.exception.InvalidDataException;
import com.example.meal_tracker.exception.NotFoundException;
import com.example.meal_tracker.repository.CategoryRepository;
import com.example.meal_tracker.repository.IngredientRepository;
import com.example.meal_tracker.repository.MealIngredientRepository;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private final MealIngredientRepository mealIngredientRepository;
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
        List<MealIngredient> mealIngredients = new ArrayList<>();
        for (MealIngredients mi : request.getMealIngredients()) {
            Ingredient ingredient = ingredientRepository.findById(mi.getIngredientId())
                    .orElseThrow(() -> new NotFoundException(String.format(INGREDIENT_NOT_FOUND,
                            mi.getIngredientId())));

            // Create join record
            MealIngredient mealIngredient = MealIngredient.builder()
                    .ingredient(ingredient)
                    .quantity(mi.getQuantity())
                    .build();
            mealIngredients.add(mealIngredient);

            // Calculate calories
            float ingredientCalories = ingredient.getCalories();
            totalCalories += (ingredientCalories * mi.getQuantity());
        }

        // Upload image to Cloudinary
        String imageUrl = imageUploadService.upload(imageFile, imageFile.getOriginalFilename());

        long now = System.currentTimeMillis();
        Meal meal = Meal.builder()
                .name(request.getMealName())
                .description(request.getMealDescription())
                .calories(totalCalories)
                .imageUrl(imageUrl)
                .mealIngredients(mealIngredients)
                .mealInstructions(request.getMealInstructions())
                .nutrition(request.getNutrition())
                .cookingTime(request.getCookingTime())
                .servings(request.getServings())
                .categories(mealCategories)
                .createdAt(now)
                .updatedAt(now)
                .build();

        for (MealIngredient mi : mealIngredients) {
            mi.setMeal(meal);
        }

        mealRepository.save(meal);

        return DtoConverter.convertToDto(meal);
    }

    @Override
    public Page<MealResponse> getMeals(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id"));
        Page<Meal> meals = mealRepository.findAll(pageable);
        return meals.map(DtoConverter::convertToDto);
    }

    @Override
    public MealResponse getMealById(Long id) throws NotFoundException {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(MEAL_NOT_FOUND, id)));

        return DtoConverter.convertToDto(meal);
    }

    @Override
    public MealResponse updateMeal(Long id, AddMealRequest request)
            throws NotFoundException, IOException, InvalidDataException {

        MultipartFile imageFile = request.getImage();

        // 1. Check meal existed
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(MEAL_NOT_FOUND, id)));

        // 2. Update meal fields
        meal.setName(request.getMealName());
        meal.setServings(request.getServings());
        meal.setCookingTime(request.getCookingTime());
        meal.setMealInstructions(request.getMealInstructions());

        // 3. Update image if new file uploaded
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = imageUploadService.upload(imageFile, imageFile.getOriginalFilename());
            meal.setImageUrl(imageUrl);
        }

        // 4. Remove old ingredient mappings
        mealIngredientRepository.deleteByMealId(id);

        float totalCalories = 0;

        // 5. Insert new ingredient mappings + recalc calories
        if (request.getMealIngredients() != null) {
            for (MealIngredients item : request.getMealIngredients()) {
                if (item.getIngredientId() == null) { // ✅ Prevent null ID
                    throw new InvalidDataException("Ingredient ID must not be null in meal ingredients!");
                }

                Ingredient ingredient = ingredientRepository.findById(item.getIngredientId())
                        .orElseThrow(() -> new NotFoundException(
                                String.format(INGREDIENT_NOT_FOUND, item.getIngredientId())));

                // Create and save mapping
                MealIngredient mi = new MealIngredient();
                mi.setMeal(meal);
                mi.setIngredient(ingredient);
                mi.setQuantity(item.getQuantity());
                mealIngredientRepository.save(mi);

                // Recalculate calories correctly
                totalCalories += item.getQuantity() * ingredient.getCalories();
            }
        }

        // 6. Update meal calories
        meal.setCalories(totalCalories);

        // 7. Save meal
        LOGGER.info("Updating meal={}", meal);
        Meal updated = mealRepository.save(meal);

        return DtoConverter.convertToDto(updated);
    }

    @Override
    public void deleteMeal(Long id) throws NotFoundException {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(MEAL_NOT_FOUND, id)));
        LOGGER.info("Deleting meal with id: {}", id);
        mealRepository.delete(meal);
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
}
