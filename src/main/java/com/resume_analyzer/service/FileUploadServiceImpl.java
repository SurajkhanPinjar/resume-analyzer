package com.resume_analyzer.service;

import com.resume_analyzer.service.FileUploadService;
import com.resume_analyzer.utils.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private final FileStorageUtil fileStorageUtil;

    @Override
    public String uploadFile(MultipartFile file) {

        try {
            return fileStorageUtil.storeFile(file);
        } catch (Exception e) {
            throw new RuntimeException("File upload failed: " + e.getMessage());
        }
    }
}