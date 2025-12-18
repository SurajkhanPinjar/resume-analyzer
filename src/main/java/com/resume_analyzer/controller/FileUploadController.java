package com.resume_analyzer.controller;

import com.resume_analyzer.dto.UploadResponse;
import com.resume_analyzer.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Tag(name = "File Upload API", description = "Upload PDFs for Some processing")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @Operation(summary = "Upload a Some file")
    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<UploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file) {

        String storedPath = fileUploadService.uploadFile(file);

        return ResponseEntity.ok(
                UploadResponse.builder()
                        .success(true)
                        .fileName(file.getOriginalFilename())
                        .storedPath(storedPath)
                        .message("File uploaded successfully")
                        .build()
        );
    }
}