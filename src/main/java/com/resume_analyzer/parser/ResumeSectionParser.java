package com.resume_analyzer.parser;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

@Component
public class ResumeSectionParser {

    public Map<String, Object> parse(String rawText) {

        Map<String, Object> result = new HashMap<>();

        String cleaned = rawText.replaceAll("\\s+", " ").trim();

        result.put("summary", extractSection(cleaned, "summary"));
        result.put("experience", extractList(cleaned, "experience"));
        result.put("skills", extractSkills(cleaned));
        result.put("education", extractList(cleaned, "education"));

        return result;
    }

    private String extractSection(String text, String keyword) {
        if (text.toLowerCase().contains(keyword)) {
            return keyword.toUpperCase() + " section parsed";
        }
        return "Not found";
    }

    private List<String> extractList(String text, String keyword) {
        List<String> list = new ArrayList<>();
        if (text.toLowerCase().contains(keyword)) {
            list.add(keyword + " item 1");
            list.add(keyword + " item 2");
        }
        return list;
    }

    private List<String> extractSkills(String text) {
        List<String> skills = new ArrayList<>();
        String[] commonSkills = {"java", "spring", "sql", "kafka", "docker"};

        for (String skill : commonSkills) {
            if (Pattern.compile("\\b" + skill + "\\b", Pattern.CASE_INSENSITIVE)
                    .matcher(text).find()) {
                skills.add(skill);
            }
        }
        return skills;
    }
}