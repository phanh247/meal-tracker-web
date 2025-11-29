package com.example.meal_tracker.controller;

import com.example.meal_tracker.dto.request.UpdateUserRequest;
import com.example.meal_tracker.dto.response.UserResponse;
import com.example.meal_tracker.exception.NotFoundException;
import com.example.meal_tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-management")
@RequiredArgsConstructor
public class UserManagementController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserManagementController.class);

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        try {
            LOGGER.info("Received request to get all users - page: {}, size: {}", page, size);
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<UserResponse> users = userService.getAllUsers(pageable);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            LOGGER.error("Error getting all users: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        try {
            LOGGER.info("Received request to get user by id: {}", id);
            UserResponse user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (NotFoundException e) {
            LOGGER.error("User not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error getting user by id: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Integer id,
            @RequestBody UpdateUserRequest request) {
        try {
            LOGGER.info("Received request to update user with id: {}", id);
            UserResponse updatedUser = userService.updateUser(id, request);
            return ResponseEntity.ok(updatedUser);
        } catch (NotFoundException e) {
            LOGGER.error("User not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error updating user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}