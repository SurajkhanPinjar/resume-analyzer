package com.resume_analyzer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadResponse {
    private boolean success;
    private String fileName;
    private String message;
    private String storedPath;
}