package com.resume_analyzer.parser;

import com.resume_analyzer.dto.JobDescriptionParsedResponse;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

@Component
public class JobDescriptionParser {

    private static final List<String> COMMON_SKILLS = List.of(
            "java", "spring", "spring boot", "kafka", "sql",
            "docker", "kubernetes", "aws", "rest", "microservices"
    );

    public JobDescriptionParsedResponse parse(String text) {

        String cleaned = text.toLowerCase();

        return JobDescriptionParsedResponse.builder()
                .role(extractRole(cleaned))
                .requiredSkills(extractSkills(cleaned))
                .experienceRange(extractExperience(cleaned))
                .keywords(extractKeywords(cleaned))
                .build();
    }

    private String extractRole(String text) {
        if (text.contains("backend")) return "Backend Engineer";
        if (text.contains("full stack")) return "Full Stack Developer";
        if (text.contains("java")) return "Java Developer";
        return "Not specified";
    }

    private List<String> extractSkills(String text) {
        List<String> skills = new ArrayList<>();
        for (String skill : COMMON_SKILLS) {
            if (Pattern.compile("\\b" + skill + "\\b").matcher(text).find()) {
                skills.add(skill);
            }
        }
        return skills;
    }

    private String extractExperience(String text) {
        if (text.contains("3–5") || text.contains("3-5")) return "3–5 years";
        if (text.contains("5+")) return "5+ years";
        if (text.contains("2+")) return "2+ years";
        return "Not specified";
    }

    private List<String> extractKeywords(String text) {
        List<String> keywords = new ArrayList<>();
        if (text.contains("microservice")) keywords.add("microservices");
        if (text.contains("api")) keywords.add("api");
        if (text.contains("scalable")) keywords.add("scalability");
        return keywords;
    }
}