package com.resume_analyzer.service;

import com.resume_analyzer.dto.AiScoreResponse;
import com.resume_analyzer.dto.CandidateScore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class BulkScoringService {

    private final AsyncAiScoringService asyncAiScoringService;

    public List<CandidateScore> scoreAllAsync(
            List<Map<String, Object>> resumes,
            String jdText,
            Double minConfidence) {

        double threshold = minConfidence != null ? minConfidence : 0.0;

        List<CompletableFuture<CandidateScore>> futures =
                resumes.stream()
                        .map(resume ->
                                asyncAiScoringService
                                        .scoreAsync(resume, jdText)
                                        .thenApply(ai ->
                                                mapToCandidate(resume, ai))
                                        .exceptionally(ex ->
                                                fallbackCandidate(resume))
                        )
                        .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .filter(cs ->
                        cs.getConfidence() != null &&
                                cs.getConfidence() >= threshold)
                .sorted((a, b) ->
                        b.getOverallScore()
                                .compareTo(a.getOverallScore()))
                .toList();
    }

    // ================= MAPPERS =================

    private CandidateScore mapToCandidate(
            Map<String, Object> resume,
            AiScoreResponse ai) {

        CandidateScore cs = new CandidateScore();
        cs.setCandidateId((String) resume.get("candidateId"));
        cs.setName((String) resume.get("name"));
        cs.setOverallScore(ai.getOverallScore());
        cs.setConfidence(ai.getConfidence());
        cs.setStrengths(ai.getStrengths());
        cs.setGaps(ai.getGaps());
        cs.setRecommendation(ai.getRecommendation());
        return cs;
    }

    private CandidateScore fallbackCandidate(
            Map<String, Object> resume) {

        CandidateScore cs = new CandidateScore();
        cs.setCandidateId((String) resume.get("candidateId"));
        cs.setName((String) resume.get("name"));
        cs.setOverallScore(0);
        cs.setConfidence(0.0);
        cs.setStrengths(List.of("AI scoring failed"));
        cs.setGaps(List.of("Retry required"));
        cs.setRecommendation("Unable to evaluate");
        return cs;
    }
}