package com.example.meal_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MealTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MealTrackerApplication.class, args);
    }
}
