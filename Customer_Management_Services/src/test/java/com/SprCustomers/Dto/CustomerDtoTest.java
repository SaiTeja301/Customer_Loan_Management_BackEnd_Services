package com.SprCustomers.Dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerDtoTest {

    @Test
    void testNoArgsConstructor() {
        CustomerDto dto = new CustomerDto();
        assertNull(dto.getCustomerId());
        assertNull(dto.getCustomerName());
        assertNull(dto.getCustomerAddress());
        assertNull(dto.getPrincipalAmount());
        assertNull(dto.getRate());
        assertNull(dto.getTime());
        assertNull(dto.getRateofInterstAmount());
        assertNull(dto.getTotalAmount());
    }

    @Test
    void testGettersAndSetters() {
        CustomerDto dto = new CustomerDto();
        dto.setCustomerId(1);
        dto.setCustomerName("John Doe");
        dto.setCustomerAddress("123 Main St");
        dto.setPrincipalAmount(1000f);
        dto.setRate(5f);
        dto.setTime(2f);
        dto.setRateofInterstAmount(100f);
        dto.setTotalAmount(1100f);

        assertEquals(1, dto.getCustomerId());
        assertEquals("John Doe", dto.getCustomerName());
        assertEquals("123 Main St", dto.getCustomerAddress());
        assertEquals(1000f, dto.getPrincipalAmount());
        assertEquals(5f, dto.getRate());
        assertEquals(2f, dto.getTime());
        assertEquals(100f, dto.getRateofInterstAmount());
        assertEquals(1100f, dto.getTotalAmount());
    }

    @Test
    void testToString() {
        CustomerDto dto = new CustomerDto();
        dto.setCustomerId(1);
        dto.setCustomerName("John Doe");
        dto.setCustomerAddress("123 Main St");
        dto.setPrincipalAmount(1000.0f);
        dto.setRate(5.0f);
        dto.setTime(2.0f);
        dto.setRateofInterstAmount(100.0f);
        dto.setTotalAmount(1100.0f);

        String expected = "CustomerDto [customerId=1, customerName=John Doe, customerAddress=123 Main St"
                + ", principalAmount=1000.0, rate=5.0, time=2.0"
                + ", rateofInterstAmount=100.0, totalAmount=1100.0]";
        assertEquals(expected, dto.toString());
    }
}
