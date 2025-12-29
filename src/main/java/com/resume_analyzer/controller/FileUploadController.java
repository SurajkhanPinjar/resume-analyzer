package com.resume_analyzer.controller;

import com.resume_analyzer.dto.UploadResponse;
import com.resume_analyzer.entity.User;
import com.resume_analyzer.service.FileUploadService;
import com.resume_analyzer.utils.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Tag(name = "File Upload API", description = "Upload resume files")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @Operation(summary = "Upload resume file (PDF)")
    @PostMapping(
            value = "/upload",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<UploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {

        // ✅ Resolve user from SESSION or API KEY
        User user = AuthUtil.currentUser(request);

        if (user == null) {
            return ResponseEntity.status(401).body(
                    UploadResponse.builder()
                            .success(false)
                            .message("Unauthorized")
                            .build()
            );
        }

        // ✅ Upload file
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