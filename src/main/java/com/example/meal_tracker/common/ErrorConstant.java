package com.example.meal_tracker.common;

public final class ErrorConstant {
    public static final String INVALID_MEAL_NAME_PARAM = "Meal name cannot be null or empty";
    public static final String INVALID_CALORIES_PARAM = "Calories cannot be negative";
    public static final String MEAL_NOT_FOUND = "Meal with id %s not found.";

    public static final String INVALID_CATEGORY_NAME_PARAM = "Category name cannot be null or empty";
    public static final String CATEGORY_EXISTED = "Category with name %s already exists";
    public static final String CATEGORY_NOT_FOUND = "Category with name %s not found.";

    public static final String DATABASE_ERROR = "Database error occurred";
    public static final String INVALID_REQUEST_DATA = "Invalid request data";

    public static final String MEAL_PLAN_EXISTED = "Meal plan with name %s already exists";

    public static final String MEAL_PLAN_NOT_FOUND = "Meal plan with id %s not found.";

    public static final String MEAL_PLAN_DAY_NOT_FOUND = "Meal plan day with id %s not found.";

    public static final String INVALID_MEAL_PLAN_NAME_PARAM = "Meal plan name cannot be null or empty";

    public static final String INVALID_MEAL_PLAN_DATE_PARAM = "Date must be format yyyy-mm-dd";

    private ErrorConstant() throws IllegalAccessException {
        throw new IllegalAccessException("Cannot instantiate utility class");
    }
}
