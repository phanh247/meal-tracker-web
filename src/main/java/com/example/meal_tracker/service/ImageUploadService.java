package com.example.meal_tracker.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageUploadService {
    String upload(MultipartFile file, String imageName) throws IOException;
}
