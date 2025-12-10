package com.example.meal_tracker.controller;

import com.example.meal_tracker.dto.request.AddMealPlanRequest;
import com.example.meal_tracker.dto.request.UpdateMealPlanRequest;
import com.example.meal_tracker.dto.response.MealPlanResponse;
import com.example.meal_tracker.exception.InvalidDataException;
import com.example.meal_tracker.exception.NotFoundException;
import com.example.meal_tracker.service.MealPlanService;
import com.example.meal_tracker.util.RequestValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/meal-plan")
@RequiredArgsConstructor
public class MealPlanController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MealPlanController.class);

    private final MealPlanService mealPlanService;

    // Tạo mới Plan
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewMealPlan(@RequestBody @Valid AddMealPlanRequest request) {
        try {
            LOGGER.info("Received request to add new meal plan: {}", request);
            RequestValidator.validateRequest(request);
            MealPlanResponse response = mealPlanService.addNewMealPlan(request);
            return ResponseEntity.ok(response);
        } catch (InvalidDataException | BadRequestException e) {
            LOGGER.error("Error adding new meal plan: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMealPlans(Pageable pageable,
            Long userId) {
        try {
            LOGGER.info("Received request to get all meal plans {} {}",
                    pageable.toString(), userId);
            return ResponseEntity.ok(mealPlanService.getMealPlans(pageable, userId));
        } catch (BadRequestException e) {
            LOGGER.error("Error deleting meal plan: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(value = "/detail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MealPlanResponse> getMealPlan(
            @PathVariable("id") Long id) throws NotFoundException {
        return ResponseEntity.ok(mealPlanService.getMealPlanById(id));
    }

    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateMealPlan(@PathVariable("id") Long id,
            @RequestBody @Valid UpdateMealPlanRequest updateMealPlanRequest) {
        try {
            LOGGER.info("Received request to update meal plan with id: {}", id);
            RequestValidator.validateRequest(updateMealPlanRequest);
            mealPlanService.updateMealPlan(id, updateMealPlanRequest);
            return ResponseEntity.ok(true);
        } catch (InvalidDataException | BadRequestException e) {
            LOGGER.error("Error updating meal plan: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteMeal(@PathVariable("id") Long id) {
        try {
            LOGGER.info("Received request to delete meal plan with id: {}", id);
            mealPlanService.deleteMealPlan(id);
            return ResponseEntity.ok(true);
        } catch (BadRequestException e) {
            LOGGER.error("Error deleting meal plan: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
