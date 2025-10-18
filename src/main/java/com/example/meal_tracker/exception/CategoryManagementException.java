package com.example.meal_tracker.exception;

public class CategoryManagementException extends Exception {
    public CategoryManagementException(String message) {
        super(message);
    }

    public CategoryManagementException(String message, Throwable cause) {
        super(message, cause);
    }
}
