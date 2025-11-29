package com.example.meal_tracker.util;

/**
 * Utility class để tính BMI (Body Mass Index)
 * BMI = weight(kg) / (height(m))^2
 */
public class BmiCalculator {

    /**
     * Tính BMI từ cân nặng và chiều cao
     * 
     * @param weight Cân nặng (kg)
     * @param height Chiều cao (cm)
     * @return BMI value, làm tròn 2 chữ số thập phân
     */
    public static double calculate(double weight, double height) {
        if (height <= 0 || weight <= 0) {
            throw new IllegalArgumentException("Chiều cao và cân nặng phải lớn hơn 0");
        }

        // Chuyển chiều cao từ cm sang m
        double heightInMeters = height / 100.0;

        // Tính BMI = weight / height^2
        double bmi = weight / (heightInMeters * heightInMeters);

        // Làm tròn 2 chữ số thập phân
        return Math.round(bmi * 100.0) / 100.0;
    }

    /**
     * Phân loại BMI theo tiêu chuẩn WHO
     * 
     * @param bmi BMI value
     * @return Phân loại BMI
     */
    public static String classify(double bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 25) {
            return "Normal";
        } else if (bmi < 30) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }
}