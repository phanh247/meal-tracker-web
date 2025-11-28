package com.example.meal_tracker.service;

import com.example.meal_tracker.dto.request.UpdateUserRequest;
import com.example.meal_tracker.dto.response.UserResponse;
import com.example.meal_tracker.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    // Get all users with pagination
    Page<UserResponse> getAllUsers(Pageable pageable);

    // Get user by ID
    UserResponse getUserById(Integer id) throws NotFoundException; // ✅ Long -> Integer

    // Update user information
    UserResponse updateUser(Integer id, UpdateUserRequest request) throws NotFoundException; // ✅ Long -> Integer
}