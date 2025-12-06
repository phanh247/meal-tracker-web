package com.example.meal_tracker.common;

public final class ErrorConstant {
    public static final String INVALID_MEAL_NAME_PARAM = "Meal name cannot be null or empty";
    public static final String INVALID_CALORIES_PARAM = "Calories cannot be negative";
    public static final String MEAL_NOT_FOUND = "Meal with id %s not found.";
    public static final String MEAL_ID_MUST_BE_NOT_NULL = "Meal ID must be not null";

    public static final String INVALID_CATEGORY_NAME_PARAM = "Category name cannot be null or empty";
    public static final String CATEGORY_EXISTED = "Category with name %s already exists";
    public static final String CATEGORY_NOT_FOUND = "Category with name %s not found.";
    public static final String INGREDIENT_NOT_FOUND = "Ingredient with name %s not found.";

    public static final String DATABASE_ERROR = "Database error occurred";
    public static final String INVALID_REQUEST_DATA = "Invalid request data";

    public static final String MEAL_PLAN_EXISTED = "Meal plan with name %s already exists";
    public static final String MEAL_PLAN_NOT_FOUND = "Meal plan with id %s not found.";
    public static final String MEAL_PLAN_ID_MUST_BE_NOT_NULL = "Meal Plan ID must be not null";
    public static final String MEAL_PLAN_ALREADY_HAD_MEAL_PLAN_ITEM = "Meal plan already had meal plan item";
    public static final String INVALID_MEAL_PLAN_NAME_PARAM = "Meal plan name cannot be null or empty";
    public static final String INVALID_START_DATE_PARAM = "Start date cannot be null or empty";
    public static final String INVALID_END_DATE_PARAM = "End date cannot be null or empty";
    public static final String INVALID_PLAN_TYPE_PARAM = "Plan type cannot be null or empty";
    public static final String INVALID_MEAL_TYPE_PARAM = "Meal type cannot be null or empty";
    public static final String INVALID_MEAL_DATE_PARAM = "Meal date is invalid";
    public static final String MEAL_DATE_MUST_BE_NOT_NULL = "Meal date must be not null";

    public static final String MEAL_PLAN_ITEM_NOT_FOUND = "Meal plan item with id %s not found.";

    public static final String USER_ID_MUST_BE_NOT_NULL = "User ID must be not null";

    private ErrorConstant() throws IllegalAccessException {
        throw new IllegalAccessException("Cannot instantiate utility class");
    }
}
