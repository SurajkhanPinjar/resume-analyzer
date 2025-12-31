package com.resume_analyzer.service;

import com.resume_analyzer.dto.CandidateScore;
import com.resume_analyzer.utils.InMemoryMultipartFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@RequiredArgsConstructor
public class ResumeZipProcessingService {

    private final ResumeParseService resumeParserService;
    private final BulkScoringService bulkScoringService;
    private final ExcelExportService excelExportService;

    public byte[] processZip(
            MultipartFile zipFile,
            String jdText,
            Double minConfidence) {

        if (jdText == null || jdText.isBlank()) {
            throw new IllegalArgumentException("Job Description is required");
        }

        // 1️⃣ Extract PDFs from ZIP
        List<MultipartFile> extractedPdfs = unzip(zipFile);

        if (extractedPdfs.isEmpty()) {
            throw new IllegalArgumentException("No valid PDF resumes found in ZIP");
        }

        // 2️⃣ Parse resumes
        List<Map<String, Object>> resumes =
                resumeParserService.parse(
                        extractedPdfs.toArray(new MultipartFile[0])
                );

        // 3️⃣ Score resumes using JD TEXT
        double threshold = minConfidence != null ? minConfidence : 0.0;

        List<CandidateScore> ranked =
                bulkScoringService.scoreAllAsync(
                        resumes,
                        jdText,
                        threshold
                );

        // 4️⃣ Export Excel
        return excelExportService.export(ranked);
    }

    /* ===================== ZIP UNZIP ===================== */
    private List<MultipartFile> unzip(MultipartFile zipFile) {

        List<MultipartFile> files = new ArrayList<>();
        Set<String> processedNames = new HashSet<>();

        try (ZipInputStream zis =
                     new ZipInputStream(zipFile.getInputStream())) {

            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {

                String entryName = entry.getName();

                // 🚫 FILTER JUNK
                if (entry.isDirectory()) continue;
                if (!entryName.toLowerCase().endsWith(".pdf")) continue;
                if (entryName.startsWith("__MACOSX")) continue;
                if (entryName.contains("/._")) continue;

                // ✅ CLEAN FILE NAME
                String fileName =
                        Paths.get(entryName).getFileName().toString();

                if (!processedNames.add(fileName)) {
                    continue; // skip duplicates
                }

                byte[] content = zis.readAllBytes();

                MultipartFile pdf =
                        new InMemoryMultipartFile(
                                "files",
                                fileName,
                                "application/pdf",
                                content
                        );

                files.add(pdf);
            }

        } catch (Exception e) {
            throw new RuntimeException("ZIP processing failed", e);
        }

        return files;
    }
}