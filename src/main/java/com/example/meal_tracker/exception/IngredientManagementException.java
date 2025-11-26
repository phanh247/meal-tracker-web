package com.example.meal_tracker.exception;

public class IngredientManagementException extends Exception {

    public IngredientManagementException() {
        super();
    }

    public IngredientManagementException(String message) {
        super(message);
    }

    public IngredientManagementException(String message, Throwable cause) {
        super(message, cause);
    }

    public IngredientManagementException(Throwable cause) {
        super(cause);
    }
}
