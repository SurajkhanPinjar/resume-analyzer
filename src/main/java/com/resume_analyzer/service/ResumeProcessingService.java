package com.resume_analyzer.service;

import com.resume_analyzer.dto.CandidateScore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResumeProcessingService {

    private final ResumeParseService resumeParserService;
    private final BulkScoringService bulkScoringService;
    private final ExcelExportService excelExportService;
    private final ObjectMapper objectMapper; // ✅ Correct Jackson mapper

    public byte[] process(
            MultipartFile[] files,
            String jdJson,
            Double minConfidence) {

        try {
            // ✅ Parse JD JSON safely
            Map<String, Object> jd =
                    objectMapper.readValue(jdJson, Map.class);

            // ✅ Parse resumes
            List<Map<String, Object>> resumes =
                    resumeParserService.parse(files);

            // ✅ Default confidence
            double confidenceThreshold =
                    minConfidence != null ? minConfidence : 0.0;

            // ✅ Score + filter inside service
            List<CandidateScore> ranked =
                    bulkScoringService.scoreAllAsync(
                            resumes,
                            jd,
                            confidenceThreshold
                    );

            // ✅ Export Excel
            return excelExportService.export(ranked);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Resume processing failed: " + e.getMessage(), e
            );
        }
    }
}