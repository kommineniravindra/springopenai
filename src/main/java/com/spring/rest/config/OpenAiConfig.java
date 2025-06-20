package com.spring.rest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;

@Configuration
public class OpenAiConfig {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @PostConstruct
    public void logKeyStatus() {
        if (openaiApiKey == null || openaiApiKey.equals("default-fallback")) {
            System.out.println("❌ OpenRouter API key NOT injected or using default!");
        } else {
            System.out.println("✅ OpenRouter API key loaded successfully.");
        }
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
            .additionalInterceptors((request, body, execution) -> {
                request.getHeaders().add("Authorization", "Bearer " + openaiApiKey);
                return execution.execute(request, body);
            })
            .build();
    }
}
