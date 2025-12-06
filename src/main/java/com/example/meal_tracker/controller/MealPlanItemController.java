package com.example.meal_tracker.controller;

import com.example.meal_tracker.dto.request.AddMealPlanItemRequest;
import com.example.meal_tracker.dto.request.UpdateMealPlanItemRequest;
import com.example.meal_tracker.dto.response.MealPlanItemResponse;
import com.example.meal_tracker.dto.response.MealPlanResponse;
import com.example.meal_tracker.exception.InvalidDataException;
import com.example.meal_tracker.service.MealPlanItemService;
import com.example.meal_tracker.util.RequestValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("/api/mealplanitem")
@RequiredArgsConstructor
public class MealPlanItemController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MealPlanItemController.class);

    private final MealPlanItemService mealPlanItemService;

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewMealPlanItem(@RequestBody @Valid AddMealPlanItemRequest request) {
        try {
            LOGGER.info("Received request to add new meal plan item: {}", request);
            RequestValidator.validateRequest(request);
            MealPlanItemResponse response = mealPlanItemService.addNewMealPlanItem(request);
            return ResponseEntity.ok(response);
        } catch (InvalidDataException | BadRequestException e) {
            LOGGER.error("Error adding new meal plan item: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateMealPlanItem(@PathVariable("id") Long id,
            @RequestBody @Valid UpdateMealPlanItemRequest updateMealPlanItemRequest) {
        try {
            LOGGER.info("Received request to update meal plan item with id: {}", id);
            RequestValidator.validateRequest(updateMealPlanItemRequest);
            mealPlanItemService.updateMealPlanItem(id, updateMealPlanItemRequest);
            return ResponseEntity.ok(true);
        } catch (InvalidDataException | BadRequestException e) {
            LOGGER.error("Error updating meal plan: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteMeal(@PathVariable("id") Long id) {
        try {
            LOGGER.info("Received request to delete meal plan item with id: {}", id);
            mealPlanItemService.deleteMealPlanItem(id);
            return ResponseEntity.ok(true);
        } catch (BadRequestException e) {
            LOGGER.error("Error deleting meal plan item: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<MealPlanItemResponse>> getMealPlanItems(Pageable pageable,
            Long mealPlanId, LocalDate date) {
        LOGGER.info("Received request to get all meal plan item {} {}", pageable.toString(), mealPlanId, date);
        return ResponseEntity.ok(mealPlanItemService.getMealPlanItems(pageable, mealPlanId, date));
    }
}
