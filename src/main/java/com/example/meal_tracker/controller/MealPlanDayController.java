package com.example.meal_tracker.controller;

import com.example.meal_tracker.dto.request.AddMealPlanDayRequest;
import com.example.meal_tracker.dto.request.UpdateMealPlanDayRequest;
import com.example.meal_tracker.dto.response.MealPlanDayResponse;
import com.example.meal_tracker.dto.response.MealPlanResponse;
import com.example.meal_tracker.exception.InvalidDataException;
import com.example.meal_tracker.service.MealPlanDayService;
import com.example.meal_tracker.util.RequestValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
@RequestMapping("/api/mealplanday")
@RequiredArgsConstructor
public class MealPlanDayController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MealPlanDayController.class);

    private final MealPlanDayService mealPlanDayService;

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewMealPlanDay(@RequestBody @Valid AddMealPlanDayRequest request) {
        try {
            LOGGER.info("Received request to add new meal plan day: {}", request);
            RequestValidator.validateRequest(request);
            MealPlanDayResponse response = mealPlanDayService.addNewMealPlanDay(request);
            return ResponseEntity.ok(response);
        } catch (InvalidDataException | BadRequestException e) {
            LOGGER.error("Error adding new meal plan day: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateMealPlanDay(@PathVariable("id") Long id,
            @RequestBody @Valid UpdateMealPlanDayRequest updateMealPlanDayRequest) {
        try {
            LOGGER.info("Received request to update meal plan day with id: {}", id);
            RequestValidator.validateRequest(updateMealPlanDayRequest);
            mealPlanDayService.updateMealPlanDay(id, updateMealPlanDayRequest);
            return ResponseEntity.ok(true);
        } catch (InvalidDataException | BadRequestException e) {
            LOGGER.error("Error updating meal plan: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteMeal(@PathVariable("id") Long id) {
        try {
            LOGGER.info("Received request to delete meal plan day with id: {}", id);
            mealPlanDayService.deleteMealPlanDay(id);
            return ResponseEntity.ok(true);
        } catch (BadRequestException e) {
            LOGGER.error("Error deleting meal plan day: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<MealPlanDayResponse>> getMealPlanDays(Pageable pageable,
            Long mealPlanId) {
        LOGGER.info("Received request to get all meal plan day {} {}", pageable.toString(), mealPlanId);
        return ResponseEntity.ok(mealPlanDayService.getMealPlanDays(pageable, mealPlanId));
    }
}
