package com.resume_analyzer.service;

import com.resume_analyzer.dto.AiScoreResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AsyncAiScoringService {

    private final AiScoringService aiScoringService;

    @Async
    public CompletableFuture<AiScoreResponse> scoreAsync(
            Map<String, Object> resume,
            String jd) {

        try {
            return CompletableFuture.completedFuture(
                    aiScoringService.score(resume, jd)
            );
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}