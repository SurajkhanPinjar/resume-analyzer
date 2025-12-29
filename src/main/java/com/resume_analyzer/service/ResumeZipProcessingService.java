package com.resume_analyzer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    public byte[] processZip(
            MultipartFile zipFile,
            String jdJson,
            Double minConfidence) {

        Map<String, Object> jd = parseJd(jdJson);

        List<MultipartFile> extractedPdfs = unzip(zipFile);

        List<Map<String, Object>> resumes =
                resumeParserService.parse(
                        extractedPdfs.toArray(new MultipartFile[0])
                );

        List<CandidateScore> ranked =
                bulkScoringService
                        .scoreAllAsync(resumes, jd, minConfidence)
                        .stream()
                        .filter(c ->
                                minConfidence == null ||
                                        c.getConfidence() >= minConfidence
                        )
                        .toList();

        return excelExportService.export(ranked);
    }

    private Map<String, Object> parseJd(String jdJson) {
        try {
            return objectMapper.readValue(jdJson, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JD JSON", e);
        }
    }

    private List<MultipartFile> unzip(MultipartFile zipFile) {
        List<MultipartFile> files = new ArrayList<>();
        Set<String> processedNames = new HashSet<>();

        try (ZipInputStream zis =
                     new ZipInputStream(zipFile.getInputStream())) {

            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {

                String entryName = entry.getName();

                // ✅ FILTER JUNK
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
                                "file",
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