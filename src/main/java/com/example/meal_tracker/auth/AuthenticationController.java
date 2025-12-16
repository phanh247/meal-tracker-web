package com.example.meal_tracker.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
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
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(service.forgotPassword(request.getEmail()));
    }

    // Verify mã OTP để reset password
    @PostMapping("/verifyotp")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpRequest request) {
        return ResponseEntity.ok(service.verifyOtp(request.getEmail(), request.getOtp()));
    }

    // Reset password
    @PostMapping("/resetpassword")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        return ResponseEntity.ok(service.resetPassword(request.getEmail(), request.getNewPassword(), request.getOtp()));
    }

    //Đăng xất
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        service.logout(token);
        return ResponseEntity.ok("Đăng xuất thành công");
    }
}