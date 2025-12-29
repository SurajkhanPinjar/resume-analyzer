package com.resume_analyzer.config;

import com.resume_analyzer.constants.ApiKeyConstants;
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

        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("API Key missing");
        }

        if (ApiKeyConstants.SWAGGER_API_KEY.equals(apiKey)) {

            User swaggerUser = new User();
            swaggerUser.setEmail("swagger@local");
            swaggerUser.setApiKey(apiKey);

            request.setAttribute("user", swaggerUser);
            return true;
        }

        // ✅ Validate real user API key
        User user = userRepository.findByApiKey(apiKey)
                .orElseThrow(() -> new RuntimeException("Invalid API key"));

        request.setAttribute("user", user);
        return true;
    }
}