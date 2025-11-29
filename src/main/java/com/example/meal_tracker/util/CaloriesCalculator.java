package com.example.meal_tracker.util;

/**
 * Utility class để tính lượng calories hàng ngày dựa trên công thức
 * Harris-Benedict
 */
public class CaloriesCalculator {

    /**
     * Tính BMR (Basal Metabolic Rate) - lượng calories cơ bản cơ thể cần
     * 
     * Nam: BMR = 88.362 + (13.397 × weight) + (4.799 × height) - (5.677 × age)
     * Nữ: BMR = 447.593 + (9.247 × weight) + (3.098 × height) - (4.330 × age)
     * 
     * @param weight Cân nặng (kg)
     * @param height Chiều cao (cm)
     * @param age    Tuổi
     * @param gender Giới tính ("male" hoặc "female")
     * @return BMR value
     */
    private static double calculateBMR(double weight, double height, int age, String gender) {
        if (gender == null) {
            throw new IllegalArgumentException("Giới tính không được null");
        }

        if (gender.equalsIgnoreCase("male")) {
            return 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
        } else if (gender.equalsIgnoreCase("female")) {
            return 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
        } else {
            throw new IllegalArgumentException("Giới tính không hợp lệ. Chỉ chấp nhận 'male' hoặc 'female'");
        }
    }

    /**
     * Lấy hệ số hoạt động dựa trên activity level
     * 
     * @param activityLevel Mức độ hoạt động
     * @return Activity multiplier
     */
    private static double getActivityMultiplier(String activityLevel) {
        if (activityLevel == null) {
            return 1.2; // Mặc định là sedentary
        }

        return switch (activityLevel.toLowerCase()) {
            case "sedentary" -> 1.2; // Ít vận động
            case "light" -> 1.375; // Vận động nhẹ (1-3 ngày/tuần)
            case "moderate" -> 1.55; // Vận động vừa (3-5 ngày/tuần)
            case "active" -> 1.725; // Vận động nhiều (6-7 ngày/tuần)
            case "very_active" -> 1.9; // Vận động rất nhiều (2 lần/ngày)
            default -> 1.2;
        };
    }

    /**
     * Điều chỉnh calories theo mục tiêu
     * 
     * @param tdee Total Daily Energy Expenditure
     * @param goal Mục tiêu ("lose_weight", "maintain", "gain_weight")
     * @return Adjusted calories
     */
    private static double adjustForGoal(double tdee, String goal) {
        if (goal == null) {
            return tdee; // Mặc định là maintain
        }

        return switch (goal.toLowerCase()) {
            case "lose_weight" -> tdee - 500; // Giảm 500 cal để giảm ~0.5kg/tuần
            case "maintain" -> tdee; // Giữ nguyên
            case "gain_weight" -> tdee + 500; // Tăng 500 cal để tăng ~0.5kg/tuần
            default -> tdee;
        };
    }

    /**
     * Tính tổng lượng calories hàng ngày cần thiết
     * 
     * @param weight        Cân nặng (kg)
     * @param height        Chiều cao (cm)
     * @param age           Tuổi
     * @param gender        Giới tính ("male" hoặc "female")
     * @param activityLevel Mức độ hoạt động
     * @param goal          Mục tiêu ("lose_weight", "maintain", "gain_weight")
     * @return Daily calories, làm tròn 2 chữ số thập phân
     */
    public static double calculate(double weight, double height, int age,
            String gender, String activityLevel, String goal) {
        if (weight <= 0 || height <= 0 || age <= 0) {
            throw new IllegalArgumentException("Cân nặng, chiều cao và tuổi phải lớn hơn 0");
        }

        // Bước 1: Tính BMR
        double bmr = calculateBMR(weight, height, age, gender);

        // Bước 2: Nhân với hệ số hoạt động để có TDEE
        double activityMultiplier = getActivityMultiplier(activityLevel);
        double tdee = bmr * activityMultiplier;

        // Bước 3: Điều chỉnh theo mục tiêu
        double dailyCalories = adjustForGoal(tdee, goal);

        // Làm tròn 2 chữ số thập phân
        return Math.round(dailyCalories * 100.0) / 100.0;
    }
}