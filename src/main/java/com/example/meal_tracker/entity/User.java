package com.example.meal_tracker.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fullName;
    @Column(unique = true)
    private String email; // Dùng email để đăng nhập
    private String username;
    private String password; // Mật khẩu được mã hóa

    // Các field thuộc chức năng reset password
    private String resetPasswordToken;
    private Date tokenExpiryDate;

    // UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(); // Do không phân chia role nên không cấu hinh
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
        // Đăng nhập và đăng ký đều email làm thông tin tài khoản chính username cho Spring Security
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }

    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public Integer getId() { return id; }
    public String getEmail() { return email; }


    // Getter/Setter cho chức năng Reset Password
    public String getResetPasswordToken() { return resetPasswordToken; }
    public void setResetPasswordToken(String resetPasswordToken) { this.resetPasswordToken = resetPasswordToken; }

    public Date getTokenExpiryDate() { return tokenExpiryDate; }
    public void setTokenExpiryDate(Date tokenExpiryDate) { this.tokenExpiryDate = tokenExpiryDate; }
}