package com.resume_analyzer.utils;

public class JsonUtils {

    public static String extractJson(String text) {

        int start = text.indexOf("{");
        int end = text.lastIndexOf("}");

        if (start == -1 || end == -1 || start >= end) {
            throw new RuntimeException("No valid JSON found in AI response");
        }

        return text.substring(start, end + 1).trim();
    }
}