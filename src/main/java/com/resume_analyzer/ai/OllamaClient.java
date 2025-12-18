package com.resume_analyzer.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class OllamaClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";

    public String callModel(String prompt) {

        Map<String, Object> request = Map.of(
                "model", "mistral",
                "prompt", prompt,
                "stream", false
        );

        Map<String, Object> rawResponse =
                restTemplate.postForObject(OLLAMA_URL, request, Map.class);

        return (String) rawResponse.get("response");
    }
}