package com.example.meal_tracker.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
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

    // Các field bổ sung cho user profile
    private String gender;
    private LocalDate birthDate;
    private Double height; // cm
    private Double weight; // kg

    // Target weight (cân nặng mục tiêu)
    @Column(name = "weight_goal")
    private Double weightGoal; // kg

    private Double bmi;
    private Double dailyCalories;
    private String activityLevel;
    private String goal;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    // UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Getters and Setters

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    // Getter/Setter cho chức năng Reset Password
    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public Date getTokenExpiryDate() {
        return tokenExpiryDate;
    }

    public void setTokenExpiryDate(Date tokenExpiryDate) {
        this.tokenExpiryDate = tokenExpiryDate;
    }

    // Getter/Setter cho user profile
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getWeightGoal() {
        return weightGoal;
    }

    public void setWeightGoal(Double weightGoal) {
        this.weightGoal = weightGoal;
    }

    /**
     * Tính chênh lệch giữa cân nặng hiện tại và mục tiêu
     * 
     * @return Số kg cần tăng/giảm (positive = cần giảm, negative = cần tăng)
     */
    public Double getWeightDifference() {
        if (weight != null && weightGoal != null) {
            return weight - weightGoal;
        }
        return null;
    }

    /**
     * Kiểm tra xem đã đạt mục tiêu cân nặng chưa
     * 
     * @param tolerance Độ sai lệch cho phép (kg), mặc định 0.5kg
     * @return true nếu đã đạt mục tiêu (trong khoảng tolerance)
     */
    public boolean isWeightGoalAchieved(Double tolerance) {
        if (weight == null || weightGoal == null) {
            return false;
        }
        if (tolerance == null) {
            tolerance = 0.5; // Mặc định chênh lệch 0.5kg là đạt mục tiêu
        }
        return Math.abs(weight - weightGoal) <= tolerance;
    }

    public Double getBmi() {
        return bmi;
    }

    public void setBmi(Double bmi) {
        this.bmi = bmi;
    }

    public Double getDailyCalories() {
        return dailyCalories;
    }

    public void setDailyCalories(Double dailyCalories) {
        this.dailyCalories = dailyCalories;
    }

    public String getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getFullName() {
        return fullName;
    }

    // PrePersist để tự động set createdAt khi insert
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = new Date();
        }
    }
}