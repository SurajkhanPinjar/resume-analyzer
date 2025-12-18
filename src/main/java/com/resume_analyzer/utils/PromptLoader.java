package com.resume_analyzer.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

public class PromptLoader {

    public static String load(String fileName) {
        try {
            ClassPathResource resource =
                    new ClassPathResource("prompts/" + fileName);

            return StreamUtils.copyToString(
                    resource.getInputStream(),
                    StandardCharsets.UTF_8
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to load prompt file", e);
        }
    }
}