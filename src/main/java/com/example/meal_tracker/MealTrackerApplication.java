package com.example.meal_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MealTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MealTrackerApplication.class, args);
    }
}
