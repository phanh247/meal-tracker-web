package com.example.meal_tracker.config;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

// Đây là nơi xử lý và trả lỗi chung
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Xử lý lỗi khi đăng ký với mật khẩu không đúng quy định
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((org.springframework.validation.FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Xử lý lỗi khi đăng nhập
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleLoginFailed(BadCredentialsException ex) {
        Map<String, String> error = new HashMap<>();

        error.put("message", "Tài khoản hoặc mật khẩu không chính xác!");
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    // 3. Xử lý lỗi db như trùng dữ liệu
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDatabaseCollision(DataIntegrityViolationException ex) {
        Map<String, String> error = new HashMap<>();

        // Lấy thông báo lỗi gốc từ Database
        String rootMsg = ex.getMostSpecificCause().getMessage();

        if (rootMsg.contains("users_username_key")) {
            error.put("message", "Tên đăng nhập (Username) này đã tồn tại!");
        } else if (rootMsg.contains("users_email_key")) { // Tùy vào tên constraint trong DB của bạn
            error.put("message", "Email này đã được đăng ký!");
        } else {
            error.put("message", "Dữ liệu không hợp lệ hoặc bị trùng lặp.");
        }
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    //Xử lý ca lỗi tổng quát khác - liên quan đến runtime
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage()); // ex.getMessage() chính là câu "Email này đã được sử dụng..."
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
