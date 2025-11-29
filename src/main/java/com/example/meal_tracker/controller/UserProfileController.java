package com.example.meal_tracker.controller;

import com.example.meal_tracker.dto.request.UpdateUserRequest;
import com.example.meal_tracker.dto.response.UserResponse;
import com.example.meal_tracker.exception.NotFoundException;
import com.example.meal_tracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserProfileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileController.class);

    private final UserService userService;

    /*
     * Get all users with pagination
     * GET /api/users?page=0&size=10&sort=id,desc
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        LOGGER.info("Received request to get all users");
        Page<UserResponse> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    /*
     * Get user by ID
     * GET /api/users/{id}
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        try {
            LOGGER.info("Received request to get user with id: {}", id);
            UserResponse response = userService.getUserById(id);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            LOGGER.error("Error retrieving user: {}", e.getMessage());
            return ResponseEntity.status(404)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    /*
     * Update user information: BMI and Daily Calories are automatically
     * recalculated
     * PUT /api/users/{id}
     */
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(
            @PathVariable Integer id,
            @RequestBody @Valid UpdateUserRequest request) {
        try {
            LOGGER.info("Received request to update user with id: {}", id);
            UserResponse response = userService.updateUser(id, request);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            LOGGER.error("Error updating user: {}", e.getMessage());
            return ResponseEntity.status(404)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    // ==================== HELPER METHOD ====================

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}