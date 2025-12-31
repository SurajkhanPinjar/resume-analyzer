//package com.resume_analyzer.controller;
//
//import com.resume_analyzer.dto.CandidateScore;
//import com.resume_analyzer.service.BulkScoringService;
//import com.resume_analyzer.service.ExcelExportService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/v1/export")
//@RequiredArgsConstructor
//public class ExportController {
//
//    private final BulkScoringService bulkScoringService;
//    private final ExcelExportService excelExportService;
//
//    @PostMapping("/excel")
//    public ResponseEntity<byte[]> exportExcel(
//            @RequestBody Map<String, Object> request) {
//
//        Map<String, Object> jd =
//                (Map<String, Object>) request.get("jd");
//
//        List<Map<String, Object>> resumes =
//                (List<Map<String, Object>>) request.get("resumes");
//
//        Double minConfidence =
//                request.get("minConfidence") != null
//                        ? Double.valueOf(request.get("minConfidence").toString())
//                        : 0.0;
//
//        List<CandidateScore> ranked =
//                bulkScoringService.scoreAllAsync(
//                        resumes, jd, minConfidence);
//
//        byte[] excel = excelExportService.export(ranked);
//
//        return ResponseEntity.ok()
//                .header("Content-Disposition",
//                        "attachment; filename=ranked_candidates.xlsx")
//                .body(excel);
//    }
//}