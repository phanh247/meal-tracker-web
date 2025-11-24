package com.example.meal_tracker.repository;

import com.example.meal_tracker.entity.PasswordHistory;
import com.example.meal_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Integer> {
    List<PasswordHistory> findByUser(User user);
}