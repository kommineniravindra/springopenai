package com.spring.rest.dto;

import lombok.Data;
import java.util.List;

@Data
public class ChatGPTResponse {
    private List<Choice> choices;
}
