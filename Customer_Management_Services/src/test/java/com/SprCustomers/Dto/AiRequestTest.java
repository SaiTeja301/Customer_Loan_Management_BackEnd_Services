package com.SprCustomers.Dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AiRequestTest {

    @Test
    void testNoArgsConstructor() {
        AiRequest request = new AiRequest();
        assertNull(request.getQuestion());
    }

    @Test
    void testAllArgsConstructor() {
        String question = "What is Spring Boot?";
        AiRequest request = new AiRequest(question);
        assertEquals(question, request.getQuestion());
    }

    @Test
    void testGettersAndSetters() {
        AiRequest request = new AiRequest();
        String question = "What is Java?";
        request.setQuestion(question);
        assertEquals(question, request.getQuestion());
    }

    @Test
    void testToString() {
        String question = "Test Question";
        AiRequest request = new AiRequest(question);
        String expected = "AiRequest [question=" + question + "]";
        assertEquals(expected, request.toString());
    }
}
