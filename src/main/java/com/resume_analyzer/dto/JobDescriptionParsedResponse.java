package com.resume_analyzer.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class JobDescriptionParsedResponse {
    private String role;
    private List<String> requiredSkills;
    private String experienceRange;
    private List<String> keywords;
}