package com.resume_analyzer.config;

import com.resume_analyzer.entity.User;
import com.resume_analyzer.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class ApiKeyInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {

        String apiKey = request.getHeader("X-API-KEY");

        if (apiKey == null) {
            throw new RuntimeException("API Key missing");
        }

        User user = userRepository.findByApiKey(apiKey)
                .orElseThrow(() -> new RuntimeException("Invalid API Key"));

        request.setAttribute("user", user);
        return true;
    }
}