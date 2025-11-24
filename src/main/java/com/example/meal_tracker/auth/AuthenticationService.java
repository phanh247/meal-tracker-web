package com.example.meal_tracker.auth;

import com.example.meal_tracker.config.EmailService;
import com.example.meal_tracker.config.JwtService;
import com.example.meal_tracker.entity.PasswordHistory;
import com.example.meal_tracker.entity.User;
import com.example.meal_tracker.repository.PasswordHistoryRepository;
import com.example.meal_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    //Repo cho lịch sử
    private final PasswordHistoryRepository historyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    //Dịch vụ gửi mail (mail service)
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;

    // Logic đăng ký
    public AuthenticationResponse register(RegisterRequest request) {
        // Check trùng mail
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email này đã được sử dụng, vui lòng nhập email khác!");
        }
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        // Mã hóa mật khẩu trước khi lưu
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        // Lưu user vào DB
        repository.save(user);
        // Tạo JWT token
        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    // Logic đăng nhập
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Xác thực người dùng bằng cách kiểm tra email + password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Nếu xác thực thành công, tìm user
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tạo JWT token
        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    //Logic reset password do quên mật khẩu
        //Gửi OTP
        public String forgotPassword(String email) {
            User user = repository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Email không tồn tại trong hệ thống"));

            // Tạo OTP ngẫu nhiên 6 số
            String otp = String.format("%06d", new Random().nextInt(999999));

            // Lưu OTP vào DB thông qua resetPasswordToken trong User.java
            user.setResetPasswordToken(otp);
            user.setTokenExpiryDate(new Date(System.currentTimeMillis() + 5 * 60 * 1000)); // Hết hạn sau 5 phút
            repository.save(user);

            // Gửi mail
            emailService.sendOtpEmail(user.getEmail(), otp);

            return "Mã OTP đã được gửi đến email của bạn.";
        }

        // Xác thực OTP
        public String verifyOtp(String email, String otp) {
            User user = repository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản người dùng"));

            if (user.getResetPasswordToken() == null || !user.getResetPasswordToken().equals(otp)) {
                throw new RuntimeException("Mã OTP không chính xác");
            }

            if (user.getTokenExpiryDate().before(new Date())) {
                throw new RuntimeException("Mã OTP đã hết hạn");
            }

            return "Xác thực thành công. Vui lòng nhập mật khẩu mới.";
        }

        // Đặt lại mật khẩu mới
        public String resetPassword(String email, String newPassword, String otp) {

            verifyOtp(email, otp); // Kiểm tra lại OTP

            User user = repository.findByEmail(email).get();

            // Kiểm tra trùng với mật khẩu hiện tại
            if (passwordEncoder.matches(newPassword, user.getPassword())) {
                throw new RuntimeException("Mật khẩu mới không được trùng với mật khẩu hiện tại!");
            }

            // Kiểm tra trùng với các mật khẩu trong quá khứ
            List<PasswordHistory> histories = historyRepository.findByUser(user);
            for (PasswordHistory history : histories) {
                if (passwordEncoder.matches(newPassword, history.getPassword())) {
                    throw new RuntimeException("Mật khẩu này đã được sử dụng. Vui lòng chọn mật khẩu khác!");
                }
            }

            // Nếu không trùng, tiến hành lưu mật khẩu
            PasswordHistory history = PasswordHistory.builder()
                    .user(user)
                    .password(user.getPassword()) // Lưu lại mật khẩu
                    .createdAt(new Date())
                    .build();
            historyRepository.save(history);

            // Cập nhật mật khẩu mới
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetPasswordToken(null); // Xóa OTP sau khi dùng xong
            user.setTokenExpiryDate(null);
            repository.save(user);

            return "Đổi mật khẩu thành công!";
    }
    //Logic reset password do người dùng muốn cập nhật
        // Gửi OTP cho người dùng
        public String requestChangePasswordOtp(String email) {
            return forgotPassword(email);
        }

        // Đổi mật khẩu chủ động
        public String changePasswordWithOtp(String email, String otp, String newPassword) {
            return resetPassword(email, newPassword, otp);
        }

}
