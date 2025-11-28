package com.example.meal_tracker.config;

import com.example.meal_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

// Đây là nơi xử lý và trả lỗi chung
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    //map lỗi
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "FAILURE");
        response.put("message", message);
        response.put("user_id", null);
        response.put("access_token", null);
        return response;
    }

    // Xử lý lỗi khi đăng ký với mật khẩu không đúng quy định
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();

        errors.put("status", "FAILURE");
        errors.put("message", "Dữ liệu không hợp lệ");
        errors.put("user_id", null);
        errors.put("access_token", null);

        // Chi tiết lỗi từng trường
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((org.springframework.validation.FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Xử lý lỗi khi đăng nhập
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleLoginFailed(BadCredentialsException ex) {
        return new ResponseEntity<>(createErrorResponse("Tài khoản hoặc mật khẩu không chính xác!"), HttpStatus.UNAUTHORIZED);
    }

    // 3. Xử lý lỗi db như trùng dữ liệu
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDatabaseCollision(DataIntegrityViolationException ex) {
        String message = "Dữ liệu không hợp lệ hoặc bị trùng lặp.";
        String rootMsg = ex.getMostSpecificCause().getMessage();
        if (rootMsg.contains("users_username_key")) {
            message = "Tên đăng nhập (Username) này đã tồn tại!";
        } else if (rootMsg.contains("users_email_key")) {
            message = "Email này đã được đăng ký!";
        }

        return new ResponseEntity<>(createErrorResponse(message), HttpStatus.CONFLICT);
    }

    //Xử lý ca lỗi tổng quát khác - liên quan đến runtime
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(createErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
