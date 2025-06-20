package com.spring.rest.dto;

import lombok.Data;
import java.util.Collections;
import java.util.List;

@Data
public class ChatGPTRequest {
    private String model;
    private List<Message> messages;

    public ChatGPTRequest(String model, String prompt) {
        this.model = model;
        this.messages = Collections.singletonList(new Message("user", prompt));
    }
}
