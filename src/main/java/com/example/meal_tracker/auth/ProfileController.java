package com.example.meal_tracker.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class ProfileController {

    private final AuthenticationService service;

    // OTP được để đổi mật khẩu
    @PostMapping("/requestotp")
    public ResponseEntity<String> requestOtp() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); //email = username_springboot

        return ResponseEntity.ok(service.requestChangePasswordOtp(currentEmail));
    }

    // Đổi mật khẩu
    @PostMapping("/changepassword")
    public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();

        return ResponseEntity.ok(service.changePasswordWithOtp(
                currentEmail,
                request.getOtp(),
                request.getNewPassword()
        ));
    }
}