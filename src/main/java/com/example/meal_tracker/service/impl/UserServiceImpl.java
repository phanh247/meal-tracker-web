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

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final Double WEIGHT_GOAL_TOLERANCE = 0.5; // 0.5kg tolerance

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
        LOGGER.info("Request data: {}", request);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User với id " + id + " không tồn tại"));

        // Cập nhật thông tin cơ bản
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
            LOGGER.info("Updated fullName: {}", request.getFullName());
        }

        if (request.getGender() != null) {
            user.setGender(request.getGender());
            LOGGER.info("Updated gender: {}", request.getGender());
        }

        if (request.getBirthDate() != null) {
            user.setBirthDate(request.getBirthDate());
            LOGGER.info("Updated birthDate: {}", request.getBirthDate());
        }

        if (request.getActivityLevel() != null) {
            user.setActivityLevel(request.getActivityLevel());
            LOGGER.info("Updated activityLevel: {}", request.getActivityLevel());
        }

        if (request.getGoal() != null) {
            user.setGoal(request.getGoal());
            LOGGER.info("Updated goal: {}", request.getGoal());
        }

        // Cập nhật weight_goal
        if (request.getWeightGoal() != null) {
            user.setWeightGoal(request.getWeightGoal());
            LOGGER.info("Updated weight goal to: {} kg", request.getWeightGoal());

            // Log progress if current weight exists
            if (user.getWeight() != null) {
                Double difference = user.getWeight() - request.getWeightGoal();
                LOGGER.info("Weight difference: {} kg ({})",
                        Math.abs(difference),
                        difference > 0 ? "needs to lose" : "needs to gain");
            }
        }

        // Cập nhật height và weight
        boolean needRecalculate = false;
        if (request.getHeight() != null) {
            user.setHeight(request.getHeight());
            LOGGER.info("Updated height: {}", request.getHeight());
            needRecalculate = true;
        }

        if (request.getWeight() != null) {
            Double oldWeight = user.getWeight();
            user.setWeight(request.getWeight());
            LOGGER.info("Updated weight: {}", request.getWeight());
            needRecalculate = true;

            // Log weight change progress
            if (oldWeight != null && user.getWeightGoal() != null) {
                Double oldDiff = Math.abs(oldWeight - user.getWeightGoal());
                Double newDiff = Math.abs(request.getWeight() - user.getWeightGoal());

                if (newDiff < oldDiff) {
                    LOGGER.info("✅ Progress toward goal! Difference reduced from {} to {} kg",
                            String.format("%.2f", oldDiff),
                            String.format("%.2f", newDiff));
                }
            }
        }

        // Tính lại BMI nếu có thay đổi height hoặc weight
        if (needRecalculate && user.getHeight() != null && user.getWeight() != null) {
            double bmi = BmiCalculator.calculate(user.getWeight(), user.getHeight());
            user.setBmi(bmi);
            LOGGER.info("Recalculated BMI: {}", bmi);
        }

        // Tính lại daily calories nếu có đủ thông tin
        if (request.getDailyCalories() != null) {
            // Nếu frontend đã tính sẵn, dùng luôn
            user.setDailyCalories(request.getDailyCalories());
            LOGGER.info("Set daily calories from request: {}", request.getDailyCalories());
        } else if (user.getWeight() != null && user.getHeight() != null &&
                user.getBirthDate() != null && user.getGender() != null &&
                user.getActivityLevel() != null && user.getGoal() != null) {
            // Nếu không có, tính lại
            int age = Period.between(user.getBirthDate(), LocalDate.now()).getYears();
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
        LOGGER.info("Updated user data: height={}, weight={}, gender={}, activityLevel={}, goal={}, dailyCalories={}",
                updatedUser.getHeight(),
                updatedUser.getWeight(),
                updatedUser.getGender(),
                updatedUser.getActivityLevel(),
                updatedUser.getGoal(),
                updatedUser.getDailyCalories());

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

        // Tính weight difference và goal achievement
        Double weightDifference = null;
        Boolean goalAchieved = false;

        if (user.getWeight() != null && user.getWeightGoal() != null) {
            weightDifference = user.getWeightDifference(); // positive = cần giảm, negative = cần tăng
            goalAchieved = user.isWeightGoalAchieved(WEIGHT_GOAL_TOLERANCE);
        }

        // Calculate age from birth_date
        Integer age = null;
        if (user.getBirthDate() != null) {
            age = Period.between(user.getBirthDate(), LocalDate.now()).getYears();
        }

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .gender(user.getGender())
                .birthDate(user.getBirthDate())
                .age(age)
                .height(user.getHeight())
                .weight(user.getWeight())
                .weightGoal(user.getWeightGoal())
                .weightDifference(weightDifference)
                .goalAchieved(goalAchieved)
                .bmi(user.getBmi())
                .bmiClassification(bmiClassification)
                .dailyCalories(user.getDailyCalories())
                .activityLevel(user.getActivityLevel())
                .goal(user.getGoal())
                .createdAt(createdAt)
                .build();
    }
}