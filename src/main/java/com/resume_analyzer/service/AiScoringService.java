package com.resume_analyzer.service;

import com.resume_analyzer.dto.AiScoreResponse;

import java.util.Map;

public interface AiScoringService {
    AiScoreResponse score(Map<String, Object> resumeJson,
                          String jdJson);
}