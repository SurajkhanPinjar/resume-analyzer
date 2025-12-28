package com.resume_analyzer.controller;

import com.resume_analyzer.service.ResumeProcessingService;
import com.resume_analyzer.service.ResumeZipProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

            @RequestPart("files")
            MultipartFile[] files,

            @RequestPart("jd")
            String jdJson,

            @RequestParam(value = "minConfidence", required = false)
            Double minConfidence
    ) {

        byte[] excel =
                resumeProcessingService.process(files, jdJson, minConfidence);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=ranked_candidates.xlsx")
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

            @RequestPart("file")
            MultipartFile zip,

            @RequestPart("jd")
            String jdJson,

            @RequestParam(value = "minConfidence", required = false)
            Double minConfidence
    ) {

        byte[] excel =
                resumeZipProcessingService.processZip(zip, jdJson, minConfidence);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=ranked_candidates.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excel);
    }
}