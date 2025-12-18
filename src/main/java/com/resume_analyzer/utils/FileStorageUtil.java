package com.resume_analyzer.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileStorageUtil {

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file) throws IOException {

        // Resolve absolute path
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

        // ✅ Create directory if not exists
        Files.createDirectories(uploadPath);

        // Resolve file path
        Path filePath = uploadPath.resolve(file.getOriginalFilename());

        // Save file
        file.transferTo(filePath.toFile());

        return filePath.toString();
    }

}