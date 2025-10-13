package com.example.meal_tracker.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users") // tránh trùng với từ khóa "user" trong PostgreSQL
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //ID

    @Column(nullable = false)
    private String name; //Ten khach hang

    @Column(nullable = false, unique = true)
    private String email; //Mail khach hang

    @Column(nullable = false)
    private String passwordHash; //matkhau

    private String gender;          //gioi_tinh
    private Integer age;           //do_tuoi
    private Double height;        //chieu_ca
    private Double weight;       //can-nang
    private String activityLevel;       //cuong-do-tap-luyen
    private String goal;        //muc-tieu

    @Column(name = "created_at")
    private LocalDateTime createdAt;    //thoi-gian-tao-tai-khoan

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
