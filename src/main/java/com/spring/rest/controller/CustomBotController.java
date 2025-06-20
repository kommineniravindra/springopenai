package com.spring.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.spring.rest.dto.ChatGPTRequest;
import com.spring.rest.dto.ChatGPTResponse;

@RestController
@CrossOrigin(origins = "*")

public class CustomBotController {

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(CustomBotController.class);

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

 
    @PostMapping("/bot/chat")
    public ResponseEntity<String> chat(@RequestParam String prompt) {
        try {
            ChatGPTRequest request = new ChatGPTRequest(model, prompt);

            HttpHeaders headers = new HttpHeaders(); // ✅ Declare headers first
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("User-Agent", "SpringApp/1.0");
            headers.add("HTTP-Referer", "https://springbackend-production-7.up.railway.app");
            headers.add("X-Title", "SpringBootBot");

            HttpEntity<ChatGPTRequest> entity = new HttpEntity<>(request, headers);
            ResponseEntity<ChatGPTResponse> response = restTemplate.postForEntity(apiURL, entity, ChatGPTResponse.class);

            if (response.getBody() != null && !response.getBody().getChoices().isEmpty()
                    && response.getBody().getChoices().get(0).getMessage() != null) {
                String answer = response.getBody().getChoices().get(0).getMessage().getContent();
                return ResponseEntity.ok(answer);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No response from OpenAI.");
            }
        } catch (Exception e) {
            logger.error("Error occurred:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

}
