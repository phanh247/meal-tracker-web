package com.example.meal_tracker.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String fullName;
    private String username;

    // === THÊM VALIDATE EMAIL ===
    @NotBlank(message = "Email không được để trống") // Không cho phép để trống email
    @Email(message = "Email không đúng định dạng")   // Kiểm tra chuẩn email có @ không
    private String email;

    //Vlidate rule cho mật khẩu
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d).{8,10}$",
            message = "Mật khẩu phải từ 8-10 ký tự, chứa ít nhất 1 chữ hoa và 1 số"
    )
    private String password;
}