package com.resume_analyzer.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ResumeParseService {
    Map<String, Object> parseResume(MultipartFile file);
}