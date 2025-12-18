package com.resume_analyzer.dto;

import lombok.Data;

import java.util.List;

@Data
public class CandidateScore {
    private String candidateId;
    private Integer overallScore;
    private Double confidence;
    private List<String> strengths;
    private List<String> gaps;
}