package com.example.meal_tracker.util;

import com.example.meal_tracker.common.ErrorConstant;
import com.example.meal_tracker.dto.request.AddCategoryRequest;
import com.example.meal_tracker.dto.request.AddMealPlanRequest;
import com.example.meal_tracker.dto.request.AddMealPlanItemRequest;
import com.example.meal_tracker.dto.request.AddMealRequest;
import com.example.meal_tracker.dto.request.UpdateMealPlanItemRequest;
import com.example.meal_tracker.dto.request.UpdateMealPlanRequest;
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
        if (request.getUserId() == null) {
            throw new InvalidDataException(ErrorConstant.USER_ID_MUST_BE_NOT_NULL);
        }
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new InvalidDataException(ErrorConstant.INVALID_MEAL_PLAN_NAME_PARAM);
        }
        if (request.getStartDate() == null) {
            throw new InvalidDataException(ErrorConstant.INVALID_START_DATE_PARAM);
        }
        if (request.getEndDate() == null) {
            throw new InvalidDataException(ErrorConstant.INVALID_END_DATE_PARAM);
        }
        if (request.getPlanType() == null) {
            throw new InvalidDataException(ErrorConstant.INVALID_PLAN_TYPE_PARAM);
        }
    }

    public static void validateRequest(UpdateMealPlanRequest request) throws InvalidDataException {
        if (request == null) {
            throw new InvalidDataException(ErrorConstant.INVALID_REQUEST_DATA);
        }
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new InvalidDataException(ErrorConstant.INVALID_MEAL_PLAN_NAME_PARAM);
        }
        if (request.getStartDate() == null) {
            throw new InvalidDataException(ErrorConstant.INVALID_START_DATE_PARAM);
        }
        if (request.getEndDate() == null) {
            throw new InvalidDataException(ErrorConstant.INVALID_END_DATE_PARAM);
        }
        if (request.getPlanType() == null) {
            throw new InvalidDataException(ErrorConstant.INVALID_PLAN_TYPE_PARAM);
        }
    }

    public static void validateRequest(AddMealPlanItemRequest request) throws InvalidDataException {
        if (request == null) {
            throw new InvalidDataException(ErrorConstant.INVALID_REQUEST_DATA);
        }
        if (request.getMealPlanId() == null) {
            throw new InvalidDataException(ErrorConstant.MEAL_PLAN_ID_MUST_BE_NOT_NULL);
        }
        if (request.getMealId() == null) {
            throw new InvalidDataException(ErrorConstant.MEAL_ID_MUST_BE_NOT_NULL);
        }
        if (request.getMealType() == null) {
            throw new InvalidDataException(ErrorConstant.INVALID_MEAL_TYPE_PARAM);
        }
        if (request.getMealDate() == null) {
            throw new InvalidDataException(ErrorConstant.MEAL_DATE_MUST_BE_NOT_NULL);
        }
    }

    public static void validateRequest(UpdateMealPlanItemRequest request) throws InvalidDataException {
        if (request == null) {
            throw new InvalidDataException(ErrorConstant.INVALID_REQUEST_DATA);
        }
        if (request.getMealType() == null) {
            throw new InvalidDataException(ErrorConstant.INVALID_MEAL_TYPE_PARAM);
        }
        if (request.getMealDate() == null) {
            throw new InvalidDataException(ErrorConstant.INVALID_MEAL_DATE_PARAM);
        }
    }
}
