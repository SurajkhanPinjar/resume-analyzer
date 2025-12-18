package com.resume_analyzer.controller;

import com.resume_analyzer.dto.ResumeParsedResponse;
import com.resume_analyzer.service.ResumeParseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/resume")
@RequiredArgsConstructor
public class ResumeParseController {

    private final ResumeParseService resumeParseService;

    @PostMapping(
            value = "/parse",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<ResumeParsedResponse> parseResume(
            @RequestParam("file") MultipartFile file) {

        Map<String, Object> parsedData = resumeParseService.parseResume(file);

        return ResponseEntity.ok(
                ResumeParsedResponse.builder()
                        .success(true)
                        .data(parsedData)
                        .build()
        );
    }
}