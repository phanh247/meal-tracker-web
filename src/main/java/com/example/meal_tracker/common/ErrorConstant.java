package com.example.meal_tracker.common;

public final class ErrorConstant {
    public static final String INVALID_MEAL_NAME_PARAM = "Meal name cannot be null or empty";
    public static final String INVALID_CALORIES_PARAM = "Calories cannot be negative";
    public static final String MEAL_NOT_FOUND = "Meal with id {} not found.";

    public static final String DATABASE_ERROR = "Database error occurred";
    public static final String INVALID_REQUEST_DATA = "Invalid request data";

    private ErrorConstant() throws IllegalAccessException {
        throw new IllegalAccessException("Cannot instantiate utility class");
    }
}
