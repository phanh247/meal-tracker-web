package com.example.meal_tracker.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth") // Prefix chung cho API
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register") //đăng ký
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Valid RegisterRequest request //@valid đ vlid mật khẩu
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login") // đăng nhập
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/forgotpassword") //quên mật khẩu
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        return ResponseEntity.ok(service.forgotPassword(email));
    }

    // Verify mã OTP để reset password
    @PostMapping("/verifyotp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        return ResponseEntity.ok(service.verifyOtp(email, otp));
    }

    // Reset password
    @PostMapping("/resetpassword")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        return ResponseEntity.ok(service.resetPassword(request.getEmail(), request.getNewPassword(), request.getOtp()));
    }
}