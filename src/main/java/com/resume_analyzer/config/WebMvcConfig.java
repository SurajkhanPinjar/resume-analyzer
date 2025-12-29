package com.resume_analyzer.config;

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

        // 🔐 API KEY interceptor (ONLY APIs)
        registry.addInterceptor(apiKeyInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/h2-console/**"   // ✅ ADD
                );

        // 🖥 SESSION interceptor (ONLY UI pages)
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
                        "/h2-console/**",  // ✅ ADD
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                );
    }
}