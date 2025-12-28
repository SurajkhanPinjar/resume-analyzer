package com.resume_analyzer.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ResumeParseService {
    Map<String, Object> parseResume(MultipartFile file);
    List<Map<String, Object>> parse(MultipartFile[] files);
}