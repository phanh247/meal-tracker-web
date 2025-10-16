package com.example.meal_tracker.controller;

import com.example.meal_tracker.entity.User;
import com.example.meal_tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {

    private final UserService userService;

    // DTO nhan du lieu dang ky
    public record RegisterRequest(String name, String email, String password) {}


    //Dang ky
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User newUser = userService.registerUser(
                    request.name(),
                    request.email(),
                    request.password()
            );
            return ResponseEntity.ok("Đăng ký thành công: " + newUser.getEmail());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}