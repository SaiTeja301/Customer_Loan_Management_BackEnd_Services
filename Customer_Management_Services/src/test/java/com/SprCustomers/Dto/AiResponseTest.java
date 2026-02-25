package com.SprCustomers.Dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class AiResponseTest {

    @Test
    void testNoArgsConstructor() {
        AiResponse response = new AiResponse();
        assertNull(response.getAnswer());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void testAllArgsConstructor() {
        String answer = "Spring Boot makes it easy to create stand-alone applications.";
        AiResponse response = new AiResponse(answer);
        assertEquals(answer, response.getAnswer());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void testGettersAndSetters() {
        AiResponse response = new AiResponse();
        String answer = "Java is an OOP language.";
        LocalDateTime timestamp = LocalDateTime.now();

        response.setAnswer(answer);
        response.setTimestamp(timestamp);

        assertEquals(answer, response.getAnswer());
        assertEquals(timestamp, response.getTimestamp());
    }

    @Test
    void testToString() {
        String answer = "Test Answer";
        AiResponse response = new AiResponse(answer);
        LocalDateTime timestamp = response.getTimestamp();
        String expected = "AiResponse [answer=" + answer + ", timestamp=" + timestamp + "]";
        assertEquals(expected, response.toString());
    }
}
