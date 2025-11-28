package com.example.meal_tracker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
//lưu những token đã bị người dùng đăng xuất
@Entity
@Table(name = "invalidated_tokens")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvalidatedToken {
    @Id
    private String id; // chuỗi token JWT
    private Date expiryTime; // thời gian hết hạn của token
}
