package com.resume_analyzer.service;

import com.resume_analyzer.parser.PdfTextExtractor;
import com.resume_analyzer.parser.ResumeSectionParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
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
}