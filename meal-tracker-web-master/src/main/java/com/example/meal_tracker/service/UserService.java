package com.example.meal_tracker.service;

import com.example.meal_tracker.entity.User;
import com.example.meal_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;  //Mã hoá mật khẩu an toàn trong Spring Security
import org.springframework.stereotype.Service;

@Service    //Đánh dấu đây là tầng dịch vụ – Spring sẽ tự quản lý và inject nơi cần dùng
@RequiredArgsConstructor //Tự động sinh constructor cho các final field → không cần viết thủ công

public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(String name, String email, String rawPassword) {

        // Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        // Mã hoá mật khẩu
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Tạo đối tượng User mới
        User user = User.builder()
                .name(name)
                .email(email)
                .passwordHash(encodedPassword)
                .build();

        // Lưu vào database
        return userRepository.save(user);
    }
}
