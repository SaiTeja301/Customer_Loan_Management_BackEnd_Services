package com.SprCustomers.Service;

/**
 * Service interface for Gemini AI operations
 */
public interface GeminiAiService {

    /**
     * Ask a question to the Gemini AI agent
     * 
     * @param question The question to ask
     * @return The AI's response
     */
    String askQuestion(String question);
}
