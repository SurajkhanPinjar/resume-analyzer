package com.resume_analyzer.controller;

import com.resume_analyzer.dto.AiScoreResponse;
import com.resume_analyzer.service.AiScoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/score")
@RequiredArgsConstructor
public class AiScoringController {

    private final AiScoringService aiScoringService;

    /**
     * Swagger-friendly endpoint to score Resume vs JD using AI
     */
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public AiScoreResponse scoreResumeAgainstJD(
            @RequestBody Map<String, Object> requestBody) {

        // Expected payload:
        // {
        //   "resume": { ... },
        //   "jd": "plain text job description"
        // }

        Object resumeObj = requestBody.get("resume");
        Object jdObj = requestBody.get("jd");

        if (!(resumeObj instanceof Map)) {
            throw new IllegalArgumentException(
                    "'resume' must be a JSON object");
        }

        if (!(jdObj instanceof String) || ((String) jdObj).isBlank()) {
            throw new IllegalArgumentException(
                    "'jd' must be a non-empty text string");
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> resume =
                (Map<String, Object>) resumeObj;

        String jdText = (String) jdObj;

        return aiScoringService.score(resume, jdText);
    }
}