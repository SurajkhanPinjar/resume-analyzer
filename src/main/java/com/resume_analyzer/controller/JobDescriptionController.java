package com.resume_analyzer.controller;

import com.resume_analyzer.dto.JobDescriptionParsedResponse;
import com.resume_analyzer.service.JobDescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/jd")
@RequiredArgsConstructor
public class JobDescriptionController {

    private final JobDescriptionService jobDescriptionService;

    @PostMapping("/parse")
    public ResponseEntity<JobDescriptionParsedResponse> parseJD(
            @RequestBody String jdText) {

        return ResponseEntity.ok(
                jobDescriptionService.parse(jdText)
        );
    }
}