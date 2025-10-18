package com.example.meal_tracker.exception;

public class MealManagementException extends Exception {
    public MealManagementException(String message) {
        super(message);
    }

    public MealManagementException(String message, Throwable cause) {
        super(message, cause);
    }
}
