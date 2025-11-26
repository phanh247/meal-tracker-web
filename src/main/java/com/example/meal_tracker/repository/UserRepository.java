package com.example.meal_tracker.repository;

import com.example.meal_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    // Spring Data JPA tự động tạo query
    Optional<User> findByEmail(String email);
}