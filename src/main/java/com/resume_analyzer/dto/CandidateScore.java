package com.resume_analyzer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CandidateScore {

    private String candidateId;
    private String name;

    private Integer overallScore;
    private Double confidence;

    private List<String> strengths;
    private List<String> gaps;

    private String recommendation;
}