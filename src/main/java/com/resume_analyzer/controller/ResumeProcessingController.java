package com.resume_analyzer.controller;

import com.resume_analyzer.service.ResumeProcessingService;
import com.resume_analyzer.service.ResumeZipProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/v1/process")
@RequiredArgsConstructor
public class ResumeProcessingController {

    private final ResumeProcessingService resumeProcessingService;
    private final ResumeZipProcessingService resumeZipProcessingService;

    /* ===================== MULTIPLE PDF UPLOAD ===================== */
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<byte[]> uploadAndProcess(

            @RequestPart("files") MultipartFile[] files,

            @RequestPart(value = "jd", required = false) String jdJson,

            @RequestPart(value = "jdFile", required = false) MultipartFile jdFile,

            @RequestParam(value = "minConfidence", required = false)
            Double minConfidence
    ) throws Exception {

        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("No resume files uploaded");
        }

        String finalJd = resolveJd(jdJson, jdFile);
        double confidence = minConfidence != null ? minConfidence : 0.0;

        byte[] excel =
                resumeProcessingService.process(files, finalJd, confidence);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=ranked_candidates.xlsx"
                )
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excel);
    }

    /* ===================== ZIP UPLOAD ===================== */
    @PostMapping(
            value = "/upload-zip",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<byte[]> uploadZip(

            @RequestPart("file") MultipartFile zip,

            @RequestPart(value = "jd", required = false) String jdJson,

            @RequestPart(value = "jdFile", required = false) MultipartFile jdFile,

            @RequestParam(value = "minConfidence", required = false)
            Double minConfidence
    ) throws Exception {

        if (zip == null || zip.isEmpty()) {
            throw new IllegalArgumentException("ZIP file is required");
        }

        String finalJd = resolveJd(jdJson, jdFile);
        double confidence = minConfidence != null ? minConfidence : 0.0;

        byte[] excel =
                resumeZipProcessingService.processZip(
                        zip,
                        finalJd,
                        confidence
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=ranked_candidates.xlsx"
                )
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excel);
    }

    /* ===================== JD RESOLUTION ===================== */
    private String resolveJd(String jdJson, MultipartFile jdFile)
            throws Exception {

        if (jdJson != null && !jdJson.isBlank()) {
            return jdJson;
        }

        if (jdFile != null && !jdFile.isEmpty()) {
            return new String(
                    jdFile.getBytes(),
                    StandardCharsets.UTF_8
            );
        }

        throw new IllegalArgumentException("Job Description is required");
    }
}