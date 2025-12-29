package com.resume_analyzer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resume_analyzer.ai.OllamaClient;
import com.resume_analyzer.dto.AiScoreResponse;
import com.resume_analyzer.service.AiScoringService;
import com.resume_analyzer.utils.JsonUtils;
import com.resume_analyzer.utils.PromptLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiScoringServiceImpl implements AiScoringService {

    private final OllamaClient ollamaClient;
    private final ObjectMapper objectMapper;

    @Override
    public AiScoreResponse score(
            Map<String, Object> resumeJson,
            Map<String, Object> jdJson) {

        String prompt = buildPrompt(resumeJson, jdJson);
        String aiRawResponse = ollamaClient.callModel(prompt);

        try {
            String cleanedJson =
                    JsonUtils.extractJson(aiRawResponse);

            return objectMapper.readValue(
                    cleanedJson, AiScoreResponse.class);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to parse AI response: " + aiRawResponse, e);
        }
    }

    private String buildPrompt(
            Map<String, Object> resume,
            Map<String, Object> jd) {

        String basePrompt =
                PromptLoader.load("resume_jd_scoring_prompt.txt");

        return basePrompt +
                "\n\nResume Data:\n" + resume +
                "\n\nJob Description Data:\n" + jd;
    }
}