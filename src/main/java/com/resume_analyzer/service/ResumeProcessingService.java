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
    private final ObjectMapper objectMapper;

    public byte[] process(
            MultipartFile[] files,
            String jdRaw,
            Double minConfidence) {

        try {
            // ✅ 1. Resolve JD (JSON or TXT)
            String jdText;
            Map<String, Object> jdMap = null;

            if (isJson(jdRaw)) {
                jdMap = objectMapper.readValue(jdRaw, Map.class);
                jdText = objectMapper.writeValueAsString(jdMap);
            } else {
                jdText = jdRaw;
            }

            // ✅ 2. Parse resumes
            List<Map<String, Object>> resumes =
                    resumeParserService.parse(files);

            // ✅ 3. Default confidence
            double confidenceThreshold =
                    minConfidence != null ? minConfidence : 0.0;

            // ✅ 4. Score resumes
            List<CandidateScore> ranked =
                    bulkScoringService.scoreAllAsync(
                            resumes,
                            jdText,   // 🔥 PASS TEXT ALWAYS
                            confidenceThreshold
                    );

            // ✅ 5. Export Excel
            return excelExportService.export(ranked);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Resume processing failed: " + e.getMessage(), e
            );
        }
    }

    // ================= HELPER =================
    private boolean isJson(String input) {
        if (input == null) return false;
        input = input.trim();
        return input.startsWith("{") && input.endsWith("}");
    }
}