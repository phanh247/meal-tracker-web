package com.example.meal_tracker.controller;

import com.example.meal_tracker.dto.request.AddMealRequest;
import com.example.meal_tracker.dto.response.MealResponse;
import com.example.meal_tracker.entity.Meal;
import com.example.meal_tracker.exception.InvalidDataException;
import com.example.meal_tracker.exception.NotFoundException;
import com.example.meal_tracker.service.MealService;
import com.example.meal_tracker.util.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/meal")
@RequiredArgsConstructor
public class MealController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MealController.class);

    private final MealService mealService;

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addNewMeal(@ModelAttribute AddMealRequest request,
                                        @RequestPart("image") MultipartFile imageFile) {
        try {
            LOGGER.info("Received request to add new meal: {}", request);
            RequestValidator.validateRequest(request);
            MealResponse response = mealService.addNewMeal(request, imageFile);
            return ResponseEntity.ok(response);
        } catch (InvalidDataException | NotFoundException | IOException e ) {
            LOGGER.error("Error adding new meal: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<MealResponse>> getMeals(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "11") int size) {
        LOGGER.info("Received request to get all meals");
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(mealService.getMeals(pageable));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MealResponse> getMealById(@PathVariable("id") Long id) {
        try {
            LOGGER.info("Received request to get meal by id: {}", id);
            MealResponse response = mealService.getMealById(id);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            LOGGER.error("Error retrieving meal: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateMeal(@PathVariable("id") Long id,
                                        @ModelAttribute AddMealRequest request) {
        try {
            LOGGER.info("Received request to update meal with info: {}", request);
            RequestValidator.validateRequest(request);
            MealResponse response = mealService.updateMeal(id, request);
            return ResponseEntity.ok(response);
        } catch (InvalidDataException | NotFoundException | IOException e) {
            LOGGER.error("Error updating meal: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteMeal(@PathVariable("id") Long id) {
        try {
            LOGGER.info("Received request to delete meal with id: {}", id);
            mealService.deleteMeal(id);
            return ResponseEntity.ok(true);
        } catch (NotFoundException e) {
            LOGGER.error("Error deleting meal: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<MealResponse>> filterMeals(
            @RequestParam(required = false) String mealName,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minCalories,
            @RequestParam(required = false) Double maxCalories,
            @RequestParam(required = false) String ingredient,
            @PageableDefault(size = 11) Pageable pageable
    ) throws NotFoundException {
        Page<MealResponse> result = mealService.filterMeals(category, mealName, minCalories, maxCalories, ingredient,
                pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{mealId}/recommendations")
    public ResponseEntity<List<MealResponse>> getRecommendations(@PathVariable Long mealId,
                                                                 @RequestParam(defaultValue = "5") int limit)
    throws NotFoundException {
        List<MealResponse> recommendations = mealService.recommendSimilarMeals(mealId, limit);
        return ResponseEntity.ok(recommendations);
    }
}
