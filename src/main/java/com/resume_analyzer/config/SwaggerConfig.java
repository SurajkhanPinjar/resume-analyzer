package com.resume_analyzer.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI baseOpenAPI() {

        // API Key definition
        SecurityScheme apiKeyScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("X-API-KEY")
                .description("Enter API Key (example: abcd-123)");

        return new OpenAPI()
                .info(new Info()
                        .title("Resume Analyzer API")
                        .version("1.0.0")
                        .description("API documentation for resume upload & processing")
                )
                // Register security scheme
                .components(new Components()
                        .addSecuritySchemes("ApiKeyAuth", apiKeyScheme)
                )
                // Apply API key globally to all endpoints
                .addSecurityItem(new SecurityRequirement()
                        .addList("ApiKeyAuth")
                );
    }
}