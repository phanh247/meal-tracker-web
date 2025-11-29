package com.example.meal_tracker.service.impl;

import com.example.meal_tracker.dto.request.UpdateUserRequest;
import com.example.meal_tracker.dto.response.UserResponse;
import com.example.meal_tracker.entity.User;
import com.example.meal_tracker.exception.NotFoundException;
import com.example.meal_tracker.repository.UserRepository;
import com.example.meal_tracker.service.UserService;
import com.example.meal_tracker.util.BmiCalculator;
import com.example.meal_tracker.util.CaloriesCalculator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        LOGGER.info("Getting all users with pagination: {}", pageable);
        Page<User> users = userRepository.findAll(pageable);
        return users.map(this::convertToResponse);
    }

    @Override
    public UserResponse getUserById(Integer id) throws NotFoundException {
        LOGGER.info("Getting user by id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User với id " + id + " không tồn tại"));
        return convertToResponse(user);
    }

    @Override
    public UserResponse updateUser(Integer id, UpdateUserRequest request) throws NotFoundException {
        LOGGER.info("Updating user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User với id " + id + " không tồn tại"));

        // Cập nhật thông tin cơ bản
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        if (request.getBirthDate() != null) {
            user.setBirthDate(request.getBirthDate());
        }
        if (request.getActivityLevel() != null) {
            user.setActivityLevel(request.getActivityLevel());
        }
        if (request.getGoal() != null) {
            user.setGoal(request.getGoal());
        }

        // Cập nhật height và weight
        boolean needRecalculate = false;
        if (request.getHeight() != null) {
            user.setHeight(request.getHeight());
            needRecalculate = true;
        }
        if (request.getWeight() != null) {
            user.setWeight(request.getWeight());
            needRecalculate = true;
        }

        // Tính lại BMI nếu có thay đổi height hoặc weight
        if (needRecalculate && user.getHeight() != null && user.getWeight() != null) {
            double bmi = BmiCalculator.calculate(user.getWeight(), user.getHeight());
            user.setBmi(bmi);
            LOGGER.info("Recalculated BMI: {}", bmi);
        }

        // Tính lại daily calories nếu có đủ thông tin
        if (user.getWeight() != null && user.getHeight() != null &&
                user.getBirthDate() != null && user.getGender() != null &&
                user.getActivityLevel() != null && user.getGoal() != null) {

            int age = java.time.Period.between(user.getBirthDate(), java.time.LocalDate.now()).getYears();
            double dailyCalories = CaloriesCalculator.calculate(
                    user.getWeight(),
                    user.getHeight(),
                    age,
                    user.getGender(),
                    user.getActivityLevel(),
                    user.getGoal());
            user.setDailyCalories(dailyCalories);
            LOGGER.info("Recalculated daily calories: {}", dailyCalories);
        }

        User updatedUser = userRepository.save(user);
        LOGGER.info("User updated successfully: {}", id);

        return convertToResponse(updatedUser);
    }

    private UserResponse convertToResponse(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String createdAt = user.getCreatedAt() != null
                ? user.getCreatedAt().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()
                        .format(formatter)
                : null;

        // Phân loại BMI
        String bmiClassification = null;
        if (user.getBmi() != null) {
            bmiClassification = BmiCalculator.classify(user.getBmi());
        }

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
                .bmiClassification(bmiClassification)
                .dailyCalories(user.getDailyCalories())
                .activityLevel(user.getActivityLevel())
                .goal(user.getGoal())
                .createdAt(createdAt)
                .build();
    }
}