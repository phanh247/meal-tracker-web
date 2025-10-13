package com.example.meal_tracker.repository;


import com.example.meal_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); //kiem tra email da ton tai chua
    boolean existsByEmail(String email);
}
