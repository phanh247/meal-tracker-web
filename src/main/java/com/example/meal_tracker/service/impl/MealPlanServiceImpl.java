package com.example.meal_tracker.service.impl;

import com.example.meal_tracker.common.ErrorConstant;
import com.example.meal_tracker.dto.request.AddMealPlanRequest;
import com.example.meal_tracker.dto.request.AddMealRequest;
import com.example.meal_tracker.dto.response.MealResponse;
import com.example.meal_tracker.entity.Category;
import com.example.meal_tracker.entity.Meal;
import com.example.meal_tracker.entity.MealPlan;
import com.example.meal_tracker.exception.CategoryManagementException;
import com.example.meal_tracker.exception.MealManagementException;
import com.example.meal_tracker.exception.NotFoundException;
import com.example.meal_tracker.repository.CategoryRepository;
import com.example.meal_tracker.repository.MealPlanRepository;
import com.example.meal_tracker.repository.MealRepository;
import com.example.meal_tracker.service.MealPlanService;
import com.example.meal_tracker.service.MealService;
import com.example.meal_tracker.util.ConverterUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class MealPlanServiceImpl implements MealPlanService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MealPlanServiceImpl.class);

    private final MealPlanRepository mealPlanRepository;

    public void addNewMealPlan(AddMealPlanRequest addMealPlanRequest) throws BadRequestException {
        // Check if mealPlanName is null or empty
        if (addMealPlanRequest.mealPlanName == null || addMealPlanRequest.mealPlanName == "") {
            LOGGER.info("Meal plan with name '{}' is empty.", addMealPlanRequest.mealPlanName);
            throw new BadRequestException(String.format(ErrorConstant.INVALID_MEAL_PLAN_NAME_PARAM));
        }

         // Find if mealPlanName already exists
        Optional<MealPlan> existingMealPlan = mealPlanRepository.findByName(addMealPlanRequest.mealPlanName);
        if (existingMealPlan.isPresent()) {
            LOGGER.info("Meal plan with name '{}' already exists.", addMealPlanRequest.mealPlanName);
            throw new BadRequestException(String.format(ErrorConstant.MEAL_PLAN_EXISTED, addMealPlanRequest.mealPlanName));
        }

        MealPlan newMealPlanEntity = ConverterUtil.convertToEntity(addMealPlanRequest);
        mealPlanRepository.save(newMealPlanEntity);
    }
   
}
