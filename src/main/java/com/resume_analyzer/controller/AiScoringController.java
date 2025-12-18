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

        // Expecting:
        // {
        //   "resume": { ... },
        //   "jd": { ... }
        // }

        Map<String, Object> resume =
                (Map<String, Object>) requestBody.get("resume");

        Map<String, Object> jd =
                (Map<String, Object>) requestBody.get("jd");

        if (resume == null || jd == null) {
            throw new IllegalArgumentException(
                    "Both 'resume' and 'jd' must be provided");
        }

        return aiScoringService.score(resume, jd);
    }
}