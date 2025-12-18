package com.resume_analyzer.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ResumeParsedResponse {
    private boolean success;
    private Map<String, Object> data;
}