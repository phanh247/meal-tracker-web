package com.example.meal_tracker.controller;

import com.example.meal_tracker.dto.request.AddMealRequest;
import com.example.meal_tracker.dto.response.MealResponse;
import com.example.meal_tracker.exception.InvalidDataException;
import com.example.meal_tracker.exception.MealManagementException;
import com.example.meal_tracker.exception.NotFoundException;
import com.example.meal_tracker.service.MealService;
import com.example.meal_tracker.util.RequestValidate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.List;

@RestController
@RequestMapping("/api/meal")
@RequiredArgsConstructor
public class MealController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MealController.class);

    private final MealService mealService;

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MealResponse> addNewMeal(@RequestBody @Valid AddMealRequest request) throws MealManagementException {
        try {
            LOGGER.info("Received request to add new meal: {}", request);
            RequestValidate.validateRequest(request);
            MealResponse response = mealService.addNewMeal(request);
            return ResponseEntity.ok(response);
        } catch (InvalidDataException e) {
            throw new MealManagementException(e.getMessage(), e);
        }
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MealResponse>> getMeals() {
        LOGGER.info("Received request to get all meals");
        List<MealResponse> response = mealService.getMeals();
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MealResponse> getMealById(@PathVariable("id") Long id) {
        LOGGER.info("Received request to get meal by id: {}", id);
        MealResponse response = mealService.getMealById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateMeal(@PathVariable("id") Long id,
                                           @RequestBody @Valid AddMealRequest request)
    throws MealManagementException {
        try {
            LOGGER.info("Received request to update meal with id: {}", id);
            RequestValidate.validateRequest(request);
            mealService.updateMeal(id, request);
            return ResponseEntity.ok(true);
        } catch (InvalidDataException | NotFoundException e) {
            throw new MealManagementException(e.getMessage(), e);
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> deleteMeal(@PathVariable("id") Long id) throws MealManagementException {
        try {
            LOGGER.info("Received request to delete meal with id: {}", id);
            mealService.deleteMeal(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            throw new MealManagementException(e.getMessage(), e);
        }
    }
}
