package com.SprCustomers.Bo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerBoTest {

    @Test
    void testGettersAndSetters() {
        CustomerBo bo = new CustomerBo();
        bo.setCustomerId(10);
        bo.setCustomerName("Alice");
        bo.setCustomerAddress("789 Pine St");
        bo.setPrincipalAmount(5000f);
        bo.setInterestRate(6f);
        bo.setTime(4f);
        bo.setRateofInterstAmount(1200f);
        bo.setTotalAmount(6200f);

        assertEquals(10, bo.getCustomerId());
        assertEquals("Alice", bo.getCustomerName());
        assertEquals("789 Pine St", bo.getCustomerAddress());
        assertEquals(5000f, bo.getPrincipalAmount());
        assertEquals(6f, bo.getInterestRate());
        assertEquals(4f, bo.getTime());
        assertEquals(1200f, bo.getRateofInterstAmount());
        assertEquals(6200f, bo.getTotalAmount());
    }

    @Test
    void testToString() {
        CustomerBo bo = new CustomerBo();
        bo.setCustomerId(10);
        bo.setCustomerName("Alice");
        bo.setCustomerAddress("789 Pine St");
        bo.setPrincipalAmount(5000.0f);
        bo.setInterestRate(6.0f);
        bo.setTime(4.0f);
        bo.setRateofInterstAmount(1200.0f);
        bo.setTotalAmount(6200.0f);

        String expected = "CustomerBo [customerId=10, customerName=Alice, customerAddress=789 Pine St"
                + ", principalAmount=5000.0, rate=6.0, time=4.0"
                + ", rateofInterstAmount=1200.0, totalAmount=6200.0]";
        assertEquals(expected, bo.toString());
    }
}
