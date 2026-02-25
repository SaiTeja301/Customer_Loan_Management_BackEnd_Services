package com.SprCustomers.Dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerUpdateRequestTest {

    @Test
    void testGettersAndSetters() {
        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setCustomerName("Jane Doe");
        request.setCustomerAddress("456 Broad St");
        request.setPrincipalAmount(2000f);
        request.setInterestRate(7f);
        request.setTime(3f);

        assertEquals("Jane Doe", request.getCustomerName());
        assertEquals("456 Broad St", request.getCustomerAddress()); // Assuming getter is getCustomerAddress(), let's
                                                                    // fix
                                                                    // if needed
        assertEquals(2000f, request.getPrincipalAmount());
        assertEquals(7f, request.getInterestRate());
        assertEquals(3f, request.getTime());
    }

    @Test
    void testProperGettersAndSetters() {
        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setCustomerName("Jane Doe");
        request.setCustomerAddress("456 Broad St");
        request.setPrincipalAmount(2000f);
        request.setInterestRate(7f);
        request.setTime(3f);

        assertEquals("Jane Doe", request.getCustomerName());
        assertEquals("456 Broad St", request.getCustomerAddress());
        assertEquals(2000f, request.getPrincipalAmount());
        assertEquals(7f, request.getInterestRate());
        assertEquals(3f, request.getTime());
    }

    @Test
    void testToString() {
        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setCustomerName("Jane Doe");
        request.setCustomerAddress("456 Broad St");
        request.setPrincipalAmount(2000.0f);
        request.setInterestRate(7.0f);
        request.setTime(3.0f);

        String expected = "CustomerUpdateRequest [customerName=Jane Doe, customerAddress=456 Broad St"
                + ", principalAmount=2000.0, interestRate=7.0, time=3.0]";
        assertEquals(expected, request.toString());
    }
}
