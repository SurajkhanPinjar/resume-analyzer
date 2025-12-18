package com.resume_analyzer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiScoreResponse {

    private Integer overallScore;
    private Double confidence;
    private List<String> strengths;
    private List<String> gaps;
    private String recommendation;
}