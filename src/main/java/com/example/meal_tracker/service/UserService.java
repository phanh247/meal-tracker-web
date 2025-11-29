package com.example.meal_tracker.service;

import com.example.meal_tracker.dto.request.UpdateUserRequest;
import com.example.meal_tracker.dto.response.UserResponse;
import com.example.meal_tracker.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserResponse> getAllUsers(Pageable pageable);

    UserResponse getUserById(Integer id) throws NotFoundException;

    UserResponse updateUser(Integer id, UpdateUserRequest request) throws NotFoundException;
}