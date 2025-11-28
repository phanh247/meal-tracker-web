package com.example.meal_tracker.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateValidator {
    public static boolean isValidDateFormat(String dateString, String formatPattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
            LocalDate.parse(dateString, formatter);
            return true; // Date string successfully parsed with the given format
        } catch (DateTimeParseException e) {
            return false; // Date string does not match the format or is not a valid date
        }
    }
}
