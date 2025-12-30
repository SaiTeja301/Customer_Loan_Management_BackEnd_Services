package com.SprCustomers.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for AI Request
 * Used for sending questions to the Gemini AI agent
 */
@Getter
@Setter
public class AiRequest {

    @NotBlank(message = "Question is required")
    private String question;

    public AiRequest() {
    }

    public AiRequest(String question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "AiRequest [question=" + question + "]";
    }
}
