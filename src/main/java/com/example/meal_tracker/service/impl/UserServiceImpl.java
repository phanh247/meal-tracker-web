package com.example.meal_tracker.service.impl;

import com.example.meal_tracker.dto.request.UpdateUserRequest;
import com.example.meal_tracker.dto.response.UserResponse;
import com.example.meal_tracker.entity.User;
import com.example.meal_tracker.exception.NotFoundException;
import com.example.meal_tracker.repository.UserRepository;
import com.example.meal_tracker.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        LOGGER.info("Fetching all users with pagination");
        return userRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Override
    public UserResponse getUserById(Long id) throws NotFoundException {
        LOGGER.info("Fetching user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        return convertToResponse(user);
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request) throws NotFoundException {
        LOGGER.info("Updating user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        // Update fields if provided
        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        if (request.getBirthDate() != null) {
            user.setBirthDate(request.getBirthDate());
        }
        if (request.getHeight() != null) {
            user.setHeight(request.getHeight());
        }
        if (request.getWeight() != null) {
            user.setWeight(request.getWeight());
        }
        if (request.getActivityLevel() != null) {
            user.setActivityLevel(request.getActivityLevel());
        }
        if (request.getGoal() != null) {
            user.setGoal(request.getGoal());
        }

        // BMI is automatically recalculated by @PreUpdate in User entity
        // Daily calories are recalculated here if enough info is available
        if (user.getHeight() != null && user.getWeight() != null &&
                user.getBirthDate() != null && user.getGender() != null) {
            user.setDailyCalories(calculateCalories(user));
        }

        user = userRepository.save(user);
        LOGGER.info("User updated successfully with id: {}. BMI: {}, Daily Calories: {}",
                id, user.getBmi(), user.getDailyCalories());

        return convertToResponse(user);
    }

    // PRIVATE HELPER METHODS

    /**
     * Calculate daily calories using Mifflin-St Jeor Equation
     */
    private Double calculateCalories(User user) {
        if (user.getHeight() == null || user.getWeight() == null ||
                user.getBirthDate() == null || user.getGender() == null) {
            return null;
        }

        int age = LocalDate.now().getYear() - user.getBirthDate().getYear();
        double bmr;

        // Calculate BMR (Basal Metabolic Rate)
        if ("male".equalsIgnoreCase(user.getGender())) {
            bmr = 10 * user.getWeight() + 6.25 * user.getHeight() - 5 * age + 5;
        } else {
            bmr = 10 * user.getWeight() + 6.25 * user.getHeight() - 5 * age - 161;
        }

        // Apply activity level multiplier
        double activityMultiplier = getActivityMultiplier(user.getActivityLevel());
        double tdee = bmr * activityMultiplier;

        // Adjust based on goal
        return adjustCaloriesForGoal(tdee, user.getGoal());
    }

    private double getActivityMultiplier(String activityLevel) {
        if (activityLevel == null)
            return 1.2;

        return switch (activityLevel.toLowerCase()) {
            case "sedentary" -> 1.2;
            case "light" -> 1.375;
            case "moderate" -> 1.55;
            case "active" -> 1.725;
            case "very_active" -> 1.9;
            default -> 1.2;
        };
    }

    private double adjustCaloriesForGoal(double tdee, String goal) {
        if (goal == null)
            return tdee;

        return switch (goal.toLowerCase()) {
            case "lose_weight" -> tdee - 500;
            case "gain_weight" -> tdee + 500;
            default -> tdee;
        };
    }

    /*
     * Convert User entity to UserResponse DTO
     */
    private UserResponse convertToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .gender(user.getGender())
                .birthDate(user.getBirthDate())
                .height(user.getHeight())
                .weight(user.getWeight())
                .bmi(user.getBmi())
                .dailyCalories(user.getDailyCalories())
                .activityLevel(user.getActivityLevel())
                .goal(user.getGoal())
                .createdAt(user.getCreatedAt())
                .build();
    }
}