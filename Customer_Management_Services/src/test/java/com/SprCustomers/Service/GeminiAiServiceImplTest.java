package com.SprCustomers.Service;

import com.SprCustomers.Dto.CustomerDto;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeminiAiServiceImplTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private GeminiAiServiceImpl geminiAiService;

    private HttpServer server;
    private int port;
    private String responseBodyData;
    private int responseCodeData;

    @BeforeEach
    void setUp() throws Exception {
        // Start a local HTTP server
        server = HttpServer.create(new InetSocketAddress(0), 0);
        port = server.getAddress().getPort();
        server.createContext("/generate", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                byte[] response = responseBodyData.getBytes("utf-8");
                exchange.sendResponseHeaders(responseCodeData, response.length);
                OutputStream os = exchange.getResponseBody();
                os.write(response);
                os.close();
            }
        });
        server.setExecutor(null);
        server.start();

        // Change the API_URL directly
        GeminiAiServiceImpl.API_URL = "http://localhost:" + port + "/generate";
    }

    @AfterEach
    void tearDown() {
        if (server != null) {
            server.stop(0);
        }
    }

    private void setApiKey(String key) throws Exception {
        Field apiKeyField = GeminiAiServiceImpl.class.getDeclaredField("apiKey");
        apiKeyField.setAccessible(true);
        apiKeyField.set(geminiAiService, key);
    }

    @Test
    void testInit() throws Exception {
        // Init just reads from env, which could be null or something
        GeminiAiServiceImpl spyService = spy(geminiAiService);
        doReturn(null).when(spyService).getEnvVariable();
        spyService.init();
        // No exception should occur
    }

    @Test
    void testAskQuestion_NoApiKey() throws Exception {
        setApiKey(null);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> geminiAiService.askQuestion("test"));
        assertEquals("GOOGLE_API_KEY environment variable is not set. Please set it and restart the application.",
                ex.getMessage());

        setApiKey("");
        assertThrows(IllegalStateException.class, () -> geminiAiService.askQuestion("test"));

        setApiKey("   ");
        assertThrows(IllegalStateException.class, () -> geminiAiService.askQuestion("test"));
    }

    @Test
    void testAskQuestion_Success() throws Exception {
        setApiKey("test-key");

        CustomerDto dto = new CustomerDto();
        dto.setCustomerId(1);
        dto.setCustomerName("Alice");
        dto.setCustomerAddress("Wonderland");
        dto.setPrincipalAmount(100f);
        dto.setRate(5f);
        dto.setTime(1f);
        dto.setTotalAmount(105f);

        when(customerService.getAllCustomerDetails()).thenReturn(Arrays.asList(dto));

        responseCodeData = 200;
        responseBodyData = "{\"candidates\": [{\"content\": {\"parts\": [{\"text\": \"This is an AI response.\"}]}}]}";

        String result = geminiAiService.askQuestion("Who is Alice?");
        assertEquals("This is an AI response.", result);
    }

    @Test
    void testAskQuestion_NoCustomers() throws Exception {
        setApiKey("test-key");

        when(customerService.getAllCustomerDetails()).thenReturn(new ArrayList<>());

        responseCodeData = 200;
        responseBodyData = "{\"candidates\": [{\"content\": {\"parts\": [{\"text\": \"No customers found response.\"}]}}]}";

        String result = geminiAiService.askQuestion("Why so empty?");
        assertEquals("No customers found response.", result);
    }

    @Test
    void testAskQuestion_ApiError() throws Exception {
        setApiKey("test-key");

        when(customerService.getAllCustomerDetails()).thenReturn(null);

        responseCodeData = 500;
        responseBodyData = "Internal Server Error from AI";

        RuntimeException ex = assertThrows(RuntimeException.class, () -> geminiAiService.askQuestion("Crash it"));
        assertTrue(ex.getMessage().contains("API Error"));
        assertTrue(ex.getMessage().contains("500"));
    }

    @Test
    void testInit_WithApiKey() {
        GeminiAiServiceImpl spyService = spy(geminiAiService);
        doReturn("valid-key").when(spyService).getEnvVariable();
        spyService.init();
        // Console output will show success
    }

    @Test
    void testInit_WithEmptyApiKey() {
        GeminiAiServiceImpl spyService = spy(geminiAiService);
        doReturn("   ").when(spyService).getEnvVariable();
        spyService.init();
        // Console output will show warning
    }

    @Test
    void testAskQuestion_NoCandidatesEmptyResponse() throws Exception {
        setApiKey("test-key");

        when(customerService.getAllCustomerDetails()).thenReturn(null);

        responseCodeData = 200;
        responseBodyData = "{\"candidates\": []}";

        String result = geminiAiService.askQuestion("Empty candidates");
        assertEquals("No response from AI", result);
    }

    @Test
    void testAskQuestion_EmptyPartsResponse() throws Exception {
        setApiKey("test-key");

        when(customerService.getAllCustomerDetails()).thenReturn(null);

        responseCodeData = 200;
        responseBodyData = "{\"candidates\": [{\"content\": {\"parts\": []}}]}";

        String result = geminiAiService.askQuestion("Empty parts");
        assertEquals("No response from AI", result);
    }
}
