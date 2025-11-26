package com.example.meal_tracker.auth;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String otp;

    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d).{8,10}$",
            message = "Mật khẩu mới phải từ 8-10 ký tự, có chữ hoa và số"
    )
    private String newPassword;
}