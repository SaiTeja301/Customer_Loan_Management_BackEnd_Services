package com.SprCustomers.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.SprCustomers.Dto.CustomerDto;

import jakarta.annotation.PostConstruct;

/**
 * Implementation of Gemini AI Service
 * Uses Google Gemini API directly with API key
 */
@Service
public class GeminiAiServiceImpl implements GeminiAiService {

    private final CustomerService customerService;
    private String apiKey;
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent";

    // Constructor Injection
    public GeminiAiServiceImpl(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostConstruct
    public void init() {
        // Get API key from environment variable
        apiKey = System.getenv("GOOGLE_API_KEY");
        if (apiKey == null || apiKey.trim().isEmpty()) {
            System.err.println("WARNING: GOOGLE_API_KEY environment variable is not set!");
            System.err.println("The /askAgent endpoint will not work without this key.");
        } else {
            System.out.println("✅ GOOGLE_API_KEY loaded successfully");
        }
    }

    @Override
    public String askQuestion(String question) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalStateException(
                    "GOOGLE_API_KEY environment variable is not set. Please set it and restart the application.");
        }

        try {
            // 1. Fetch all customer data
            List<CustomerDto> customers = customerService.getAllCustomerDetails();

            // 2. Create context string
            StringBuilder context = new StringBuilder();
            context.append("You are a helpful assistant for a Customer Management System.\n");
            context.append("Here is the current customer data in the database:\n");
            context.append("--------------------------------------------------\n");

            if (customers != null && !customers.isEmpty()) {
                for (CustomerDto c : customers) {
                    context.append(String.format(
                            "ID: %d, Name: %s, Address: %s, Principal: %.2f, Rate: %.2f, Time: %.2f, Total: %.2f\n",
                            c.getCustomerId(), c.getCustomerName(), c.getCustomerAddress(),
                            c.getPrincipalAmount(), c.getRate(), c.getTime(), c.getTotalAmount()));
                }
            } else {
                context.append("No customers found in the database.\n");
            }
            context.append("--------------------------------------------------\n");
            context.append("Answer the following question based ONLY on the data provided above.\n");
            context.append("User Question: ").append(question);

            // 3. Call AI with context
            return callGeminiApi(context.toString());

        } catch (Exception e) {
            throw new RuntimeException("Error calling Gemini AI: " + e.getMessage(), e);
        }
    }

    /**
     * Call the Gemini API with the question using direct HTTP request
     */
    private String callGeminiApi(String question) throws Exception {
        String urlString = API_URL + "?key=" + apiKey;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            // Set up the request
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Create request body
            JSONObject requestBody = new JSONObject();
            JSONArray contents = new JSONArray();
            JSONObject content = new JSONObject();
            JSONArray parts = new JSONArray();
            JSONObject part = new JSONObject();
            part.put("text", question);
            parts.put(part);
            content.put("parts", parts);
            contents.put(content);
            requestBody.put("contents", contents);

            // Send request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Read response
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    // Parse response
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONArray candidates = jsonResponse.getJSONArray("candidates");
                    if (candidates.length() > 0) {
                        JSONObject candidate = candidates.getJSONObject(0);
                        JSONObject contentObj = candidate.getJSONObject("content");
                        JSONArray partsArray = contentObj.getJSONArray("parts");
                        if (partsArray.length() > 0) {
                            return partsArray.getJSONObject(0).getString("text");
                        }
                    }
                    return "No response from AI";
                }
            } else {
                // Read error response
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream(), "utf-8"))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        errorResponse.append(responseLine.trim());
                    }
                    throw new RuntimeException("API Error (HTTP " + responseCode + "): " + errorResponse.toString());
                }
            }
        } finally {
            conn.disconnect();
        }
    }
}
