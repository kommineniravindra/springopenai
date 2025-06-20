package com.spring.rest.controller;

import com.spring.rest.dto.ChatGPTRequest;
import com.spring.rest.dto.ChatGPTResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@CrossOrigin(origins = "*") 
@RestController
@RequestMapping("/bot")
public class CustomBotController {
	
	@Autowired
    private  RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(CustomBotController.class);

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;
    
    @Value("${openai.api.key}")
    private String apiKey;
    

    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam String prompt) {
        try {
            ChatGPTRequest request = new ChatGPTRequest(model, prompt);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("HTTP-Referer", "http://localhost:8080");
            headers.add("X-Title", "SpringBootBot");
            headers.setBearerAuth(apiKey);
            
            

            HttpEntity<ChatGPTRequest> entity = new HttpEntity<>(request, headers);
            ResponseEntity<ChatGPTResponse> response = restTemplate.postForEntity(apiURL, entity, ChatGPTResponse.class);

            if (response.getBody() != null && !response.getBody().getChoices().isEmpty() &&response.getBody().getChoices().get(0).getMessage() != null) {
                String answer = response.getBody().getChoices().get(0).getMessage().getContent();
                return ResponseEntity.ok(answer);
            } 
            else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No response from OpenAI.");
            }
        } 
        catch (Exception e) {
            logger.error("Exception: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            		             .body("Error: " + e.getMessage());
        }
    }
//    @GetMapping("/chat")
//    public ResponseEntity<String> chatGet(@RequestParam String prompt) {
//        return chat(prompt); // call your existing POST method
//    }
}
