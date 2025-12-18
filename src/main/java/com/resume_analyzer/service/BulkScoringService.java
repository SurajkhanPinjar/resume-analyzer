package com.resume_analyzer.service;

import com.resume_analyzer.dto.AiScoreResponse;
import com.resume_analyzer.dto.CandidateScore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BulkScoringService {

    private final AiScoringService aiScoringService;

    public List<CandidateScore> scoreAll(
            List<Map<String, Object>> resumes,
            Map<String, Object> jd) {

        return resumes.stream()
                .map(resume -> {

                    AiScoreResponse ai =
                            aiScoringService.score(resume, jd);

                    CandidateScore cs = new CandidateScore();

                    cs.setCandidateId((String) resume.get("candidateId"));
                    cs.setName((String) resume.get("name")); // ✅ FIX HERE

                    cs.setOverallScore(ai.getOverallScore());
                    cs.setConfidence(ai.getConfidence());
                    cs.setStrengths(ai.getStrengths());
                    cs.setGaps(ai.getGaps());
                    cs.setRecommendation(ai.getRecommendation());

                    return cs;
                })
                .sorted((a, b) ->
                        b.getOverallScore().compareTo(a.getOverallScore()))
                .toList();
    }
}