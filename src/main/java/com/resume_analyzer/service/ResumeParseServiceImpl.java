package com.resume_analyzer.service;

import com.resume_analyzer.parser.PdfTextExtractor;
import com.resume_analyzer.parser.ResumeSectionParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResumeParseServiceImpl implements ResumeParseService {

    private final PdfTextExtractor pdfTextExtractor;
    private final ResumeSectionParser resumeSectionParser;

    @Override
    public Map<String, Object> parseResume(MultipartFile file) {

        try (InputStream inputStream = file.getInputStream()) {

            String rawText = pdfTextExtractor.extractText(inputStream);

            return resumeSectionParser.parse(rawText);

        } catch (Exception e) {
            throw new RuntimeException("Resume parsing failed");
        }
    }


    public List<Map<String, Object>> parse(MultipartFile[] files) {

        List<Map<String, Object>> resumes = new ArrayList<>();

        int counter = 1;

        for (MultipartFile file : files) {

            Map<String, Object> resume = new HashMap<>();

            // 1️⃣ Candidate ID
            String candidateId = "CAND-" + String.format("%03d", counter++);
            resume.put("candidateId", candidateId);

            // 2️⃣ Name (from filename fallback)
            String fileName = file.getOriginalFilename();
            String name = extractNameFromFile(fileName);
            resume.put("name", name);

            // 3️⃣ Extract raw text (stub for now)
            String rawText = extractText(file);
            resume.put("rawText", rawText);

            // 4️⃣ Structured fields (basic version)
            resume.put("skills", extractSkills(rawText));
            resume.put("experience", extractExperience(rawText));
            resume.put("education", extractEducation(rawText));

            resumes.add(resume);
        }

        return resumes;
    }

    // ---------------- HELPERS ----------------

    private String extractNameFromFile(String fileName) {
        if (fileName == null) return "Unknown";

        return fileName
                .replace(".pdf", "")
                .replace("_", " ")
                .replace("-", " ")
                .trim();
    }

    private String extractText(MultipartFile file) {
        // 🔥 Plug PDFBox / Tika here later
        return "Sample resume text for " + file.getOriginalFilename();
    }

    private List<String> extractSkills(String text) {
        return List.of("Java", "Spring Boot", "Kafka");
    }

    private List<String> extractExperience(String text) {
        return List.of("Backend Engineer at FinTech Startup");
    }

    private List<String> extractEducation(String text) {
        return List.of("B.Tech Computer Science");
    }
}