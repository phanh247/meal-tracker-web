package com.example.meal_tracker.util;

import com.example.meal_tracker.common.ErrorConstant;
import com.example.meal_tracker.dto.request.AddMealRequest;
import com.example.meal_tracker.exception.InvalidDataException;

import static com.example.meal_tracker.common.ErrorConstant.INVALID_CALORIES_PARAM;

public class RequestValidate {

    public static void validateRequest(AddMealRequest request) throws InvalidDataException {
        if (request == null) {
            throw new InvalidDataException(ErrorConstant.INVALID_REQUEST_DATA);
        }
        if (request.getMealName() == null || request.getMealName().isEmpty()) {
            throw new InvalidDataException(ErrorConstant.INVALID_MEAL_NAME_PARAM);
        }
        if (request.getCalories() < 0) {
            throw new InvalidDataException(INVALID_CALORIES_PARAM);
        }
    }
}
