package com.resume_analyzer.controller;

import com.resume_analyzer.dto.BulkScoreResponse;
import com.resume_analyzer.service.BulkScoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/score")
@RequiredArgsConstructor
public class BulkScoringController {

    private final BulkScoringService bulkScoringService;

    @PostMapping("/bulk")
    public BulkScoreResponse bulkScore(
            @org.springframework.web.bind.annotation.RequestBody
            Map<String, Object> request) {

        Map<String, Object> jd =
                (Map<String, Object>) request.get("jd");

        List<Map<String, Object>> resumes =
                (List<Map<String, Object>>) request.get("resumes");

        // ✅ Read minConfidence (optional)
        Double minConfidence =
                request.get("minConfidence") != null
                        ? Double.valueOf(request.get("minConfidence").toString())
                        : 0.0;

        BulkScoreResponse response = new BulkScoreResponse();
        response.setResults(
                bulkScoringService.scoreAllAsync(
                        resumes,
                        jd,
                        minConfidence
                )
        );

        return response;
    }
}