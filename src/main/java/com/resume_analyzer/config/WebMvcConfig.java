package com.resume_analyzer.config;

import com.resume_analyzer.config.ApiKeyInterceptor;
import com.resume_analyzer.config.SessionInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final ApiKeyInterceptor apiKeyInterceptor;
    private final SessionInterceptor sessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 🔐 API security (API Key)
        registry.addInterceptor(apiKeyInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                );

        // 🖥 UI session security (ONLY UI PAGES)
        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns(
                        "/upload",
                        "/usage",
                        "/dashboard"
                )
                .excludePathPatterns(
                        "/login",
                        "/signup",
                        "/logout",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                );
    }
}