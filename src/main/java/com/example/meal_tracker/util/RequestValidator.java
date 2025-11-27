package com.example.meal_tracker.util;

import com.example.meal_tracker.common.ErrorConstant;
import com.example.meal_tracker.dto.request.AddCategoryRequest;
import com.example.meal_tracker.dto.request.AddMealPlanDayRequest;
import com.example.meal_tracker.dto.request.AddMealPlanRequest;
import com.example.meal_tracker.dto.request.AddMealRequest;
import com.example.meal_tracker.dto.request.UpdateMealPlanRequest;
import com.example.meal_tracker.dto.request.UpdateMealPlanDayRequest;
import com.example.meal_tracker.exception.InvalidDataException;

import static com.example.meal_tracker.common.ErrorConstant.INVALID_CALORIES_PARAM;

public class RequestValidator {

    public static void validateRequest(AddMealRequest request) throws InvalidDataException {
        if (request == null) {
            throw new InvalidDataException(ErrorConstant.INVALID_REQUEST_DATA);
        }
        if (request.getMealName() == null || request.getMealName().isEmpty()) {
            throw new InvalidDataException(ErrorConstant.INVALID_MEAL_NAME_PARAM);
        }
    }

    public static void validateRequest(AddCategoryRequest request) throws InvalidDataException {
        if (request == null) {
            throw new InvalidDataException(ErrorConstant.INVALID_REQUEST_DATA);
        }
        if (request.getCategoryName() == null || request.getCategoryName().isEmpty()) {
            throw new InvalidDataException(ErrorConstant.INVALID_CATEGORY_NAME_PARAM);
        }
    }

    public static void validateRequest(AddMealPlanRequest request) throws InvalidDataException {
        if (request == null) {
            throw new InvalidDataException(ErrorConstant.INVALID_REQUEST_DATA);
        }
        if (request.getMealPlanName() == null || request.getMealPlanName().isEmpty()) {
            throw new InvalidDataException(ErrorConstant.INVALID_MEAL_PLAN_NAME_PARAM);
        }
    }

    public static void validateRequest(UpdateMealPlanRequest request) throws InvalidDataException {
        if (request == null) {
            throw new InvalidDataException(ErrorConstant.INVALID_REQUEST_DATA);
        }
    }

    public static void validateRequest(AddMealPlanDayRequest request) throws InvalidDataException {
        if (request == null) {
            throw new InvalidDataException(ErrorConstant.INVALID_REQUEST_DATA);
        }

        if (DateValidator.isValidDateFormat(request.date, "yyyy-mm-dd") == false)
            throw new InvalidDataException(ErrorConstant.INVALID_MEAL_PLAN_DATE_PARAM);
    }

    public static void validateRequest(UpdateMealPlanDayRequest request) throws InvalidDataException {
        if (request == null) {
            throw new InvalidDataException(ErrorConstant.INVALID_REQUEST_DATA);
        }
    }
}
