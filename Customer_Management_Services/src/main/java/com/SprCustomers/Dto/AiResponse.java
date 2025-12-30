package com.SprCustomers.Dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for AI Response
 * Used for returning answers from the Gemini AI agent
 */
@Getter
@Setter
public class AiResponse {

    private String answer;
    private LocalDateTime timestamp;

    public AiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public AiResponse(String answer) {
        this.answer = answer;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "AiResponse [answer=" + answer + ", timestamp=" + timestamp + "]";
    }
}
