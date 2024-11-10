package com.example.lostandfound.service;

import jakarta.persistence.criteria.Path;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileUploadService {

    private static final String UPLOAD_DIR = "/home/pictures/"; // specify your path

    // Method to handle file uploads
    public String uploadFile(MultipartFile file) throws IOException {
        // Generate a unique filename (e.g., based on timestamp)
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // Create file path
        Path filePath = (Path) Paths.get(UPLOAD_DIR, filename);

        // Save the file
        Files.copy((java.nio.file.Path) file.getInputStream(), (java.nio.file.Path) filePath, StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }
}
