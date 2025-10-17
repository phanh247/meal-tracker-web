package com.example.meal_tracker.controller;

import com.example.meal_tracker.dto.request.AddMealRequest;
import com.example.meal_tracker.dto.response.MealResponse;
import com.example.meal_tracker.exception.InvalidDataException;
import com.example.meal_tracker.service.MealService;
import com.example.meal_tracker.util.RequestValidate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/meal")
@RequiredArgsConstructor
public class MealController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MealController.class);

    private final MealService mealService;

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MealResponse> addNewMeal(@RequestBody @Valid AddMealRequest request) throws InvalidDataException {
        LOGGER.info("Received request to add new meal: {}", request);
        RequestValidate.validateRequest(request);
        MealResponse response = mealService.addNewMeal(request);
        return ResponseEntity.ok(response);
    }
}
