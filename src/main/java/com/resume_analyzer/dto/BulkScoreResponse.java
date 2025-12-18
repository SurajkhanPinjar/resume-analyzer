package com.resume_analyzer.dto;

import lombok.Data;
import java.util.List;

@Data
public class BulkScoreResponse {
    private List<CandidateScore> results;
}