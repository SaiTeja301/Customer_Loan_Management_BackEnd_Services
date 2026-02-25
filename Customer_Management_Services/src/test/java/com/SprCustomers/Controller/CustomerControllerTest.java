package com.SprCustomers.Controller;

import com.SprCustomers.Dto.AiRequest;
import com.SprCustomers.Dto.CustomerDto;
import com.SprCustomers.Dto.CustomerUpdateRequest;
import com.SprCustomers.Service.CustomerService;
import com.SprCustomers.Service.GeminiAiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private GeminiAiService geminiAiService;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomerDto sampleDto;

    @BeforeEach
    void setUp() {
        sampleDto = new CustomerDto();
        sampleDto.setCustomerId(1);
        sampleDto.setCustomerName("John Doe");
        sampleDto.setCustomerAddress("New York");
        sampleDto.setPrincipalAmount(1000f);
        sampleDto.setRate(5f);
        sampleDto.setTime(2f);
        sampleDto.setRateofInterstAmount(100f);
        sampleDto.setTotalAmount(1100f);
    }

    // ================= AI AGENT TESTS ====================
    @Test
    void testAskAgent_Success() throws Exception {
        AiRequest request = new AiRequest("Hello AI");
        when(geminiAiService.askQuestion("Hello AI")).thenReturn("Hi there!");

        mockMvc.perform(post("/Spr/customers/askAgent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value("Hi there!"));
    }

    @Test
    void testAskAgent_IllegalStateException() throws Exception {
        AiRequest request = new AiRequest("Hello AI");
        when(geminiAiService.askQuestion("Hello AI")).thenThrow(new IllegalStateException("API key missing"));

        mockMvc.perform(post("/Spr/customers/askAgent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.answer").value("❌ Error: API key missing"));
    }

    @Test
    void testAskAgent_GenericException() throws Exception {
        AiRequest request = new AiRequest("Hello AI");
        when(geminiAiService.askQuestion("Hello AI")).thenThrow(new RuntimeException("Network error"));

        mockMvc.perform(post("/Spr/customers/askAgent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.answer").value("❌ Error communicating with AI service: Network error"));
    }

    // ================= GET CUSTOMER BY ID TESTS ====================
    @Test
    void testGetCustomerById_Found() throws Exception {
        when(customerService.getCustomerDetailsById(1)).thenReturn(sampleDto);

        mockMvc.perform(get("/Spr/customers/getCustomerById").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("1"))
                .andExpect(jsonPath("$.customerName").value("John Doe"));
    }

    @Test
    void testGetCustomerById_NotFound() throws Exception {
        when(customerService.getCustomerDetailsById(99)).thenReturn(null);

        mockMvc.perform(get("/Spr/customers/getCustomerById").param("id", "99"))
                .andExpect(status().isNotFound());
    }

    // ================= GET ALL CUSTOMERS TESTS ====================
    @Test
    void testGetAllCustomers_Found() throws Exception {
        when(customerService.getAllCustomerDetails()).thenReturn(Arrays.asList(sampleDto));

        mockMvc.perform(get("/Spr/customers/getAllCustomers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value("1"));
    }

    @Test
    void testGetAllCustomers_NoContent() throws Exception {
        when(customerService.getAllCustomerDetails()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/Spr/customers/getAllCustomers"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllCustomers_NullReturn() throws Exception {
        when(customerService.getAllCustomerDetails()).thenReturn(null);

        mockMvc.perform(get("/Spr/customers/getAllCustomers"))
                .andExpect(status().isNoContent());
    }

    // ================= DELETE CUSTOMER TESTS ====================
    @Test
    void testDeleteCustomer_Success() throws Exception {
        when(customerService.getCustomerDetailsById(1)).thenReturn(sampleDto);
        when(customerService.deleteCustomerById(1)).thenReturn(1);

        mockMvc.perform(delete("/Spr/customers/deleteCustomerById/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Customer Deleted Successfully")));
    }

    @Test
    void testDeleteCustomer_NotFound() throws Exception {
        when(customerService.getCustomerDetailsById(99)).thenReturn(null);

        mockMvc.perform(delete("/Spr/customers/deleteCustomerById/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("❌ Customer not found with ID: 99"));
    }

    @Test
    void testDeleteCustomer_Exception() throws Exception {
        when(customerService.getCustomerDetailsById(1)).thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(delete("/Spr/customers/deleteCustomerById/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("❌ Error while deleting customer: DB error"));
    }

    // ================= UPDATE CUSTOMER TESTS ====================
    @Test
    void testUpdateCustomer_Success() throws Exception {
        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setCustomerName("Jane");

        when(customerService.updateCustomerById(eq(1), any(CustomerUpdateRequest.class))).thenReturn(sampleDto);

        mockMvc.perform(put("/Spr/customers/updateCustomerById")
                .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Customer Updated Successfully")));
    }

    @Test
    void testUpdateCustomer_NotFound() throws Exception {
        CustomerUpdateRequest request = new CustomerUpdateRequest();

        when(customerService.updateCustomerById(eq(99), any(CustomerUpdateRequest.class))).thenReturn(null);

        mockMvc.perform(put("/Spr/customers/updateCustomerById")
                .param("id", "99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("❌ Customer with ID 99 not found."));
    }

    @Test
    void testUpdateCustomer_Exception() throws Exception {
        CustomerUpdateRequest request = new CustomerUpdateRequest();

        when(customerService.updateCustomerById(eq(1), any(CustomerUpdateRequest.class)))
                .thenThrow(new RuntimeException("Update error"));

        mockMvc.perform(put("/Spr/customers/updateCustomerById")
                .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("❌ Error while updating customer: Update error"));
    }

    // ================= INSERT CUSTOMER TESTS ====================
    @Test
    void testInsertCustomer_Success() throws Exception {
        when(customerService.insertCustomer(any(CustomerDto.class))).thenReturn(sampleDto);

        mockMvc.perform(post("/Spr/customers/insertCustomer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Customer Inserted Successfully")));
    }

    @Test
    void testInsertCustomer_InvalidData() throws Exception {
        when(customerService.insertCustomer(any(CustomerDto.class))).thenReturn(null);

        mockMvc.perform(post("/Spr/customers/insertCustomer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("❌ Invalid customer data provided."));
    }

    @Test
    void testInsertCustomer_IllegalArgument() throws Exception {
        when(customerService.insertCustomer(any(CustomerDto.class)))
                .thenThrow(new IllegalArgumentException("Missing parameter"));

        mockMvc.perform(post("/Spr/customers/insertCustomer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("❌ Missing parameter"));
    }

    @Test
    void testInsertCustomer_Exception() throws Exception {
        when(customerService.insertCustomer(any(CustomerDto.class))).thenThrow(new RuntimeException("Save failed"));

        mockMvc.perform(post("/Spr/customers/insertCustomer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("❌ Error while inserting customer: Save failed"));
    }
}
