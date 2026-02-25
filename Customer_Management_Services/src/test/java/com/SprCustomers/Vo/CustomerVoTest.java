package com.SprCustomers.Vo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerVoTest {

    @Test
    void testNoArgsConstructor() {
        CustomerVo vo = new CustomerVo();
        assertNull(vo.getCustomerId());
        assertNull(vo.getCustomerName());
        assertNull(vo.getCustomerAddress());
        assertNull(vo.getPrincipalAmount());
        assertNull(vo.getRate());
        assertNull(vo.getTime());
        assertNull(vo.getRateofInterstAmount());
        assertNull(vo.getTotalAmount());
    }

    @Test
    void testGettersAndSetters() {
        CustomerVo vo = new CustomerVo();
        vo.setCustomerId("1");
        vo.setCustomerName("Bob");
        vo.setCustomerAddress("321 Oak St");
        vo.setPrincipalAmount("1000.0");
        vo.setRate("5.0");
        vo.setTime("2.0");
        vo.setRateofInterstAmount("100.0");
        vo.setTotalAmount("1100.0");

        assertEquals("1", vo.getCustomerId());
        assertEquals("Bob", vo.getCustomerName());
        assertEquals("321 Oak St", vo.getCustomerAddress());
        assertEquals("1000.0", vo.getPrincipalAmount());
        assertEquals("5.0", vo.getRate());
        assertEquals("2.0", vo.getTime());
        assertEquals("100.0", vo.getRateofInterstAmount());
        assertEquals("1100.0", vo.getTotalAmount());
    }

    @Test
    void testToString() {
        CustomerVo vo = new CustomerVo();
        vo.setCustomerId("1");
        vo.setCustomerName("Bob");
        vo.setCustomerAddress("321 Oak St");
        vo.setPrincipalAmount("1000.0");
        vo.setRate("5.0");
        vo.setTime("2.0");
        vo.setRateofInterstAmount("100.0");
        vo.setTotalAmount("1100.0");

        String expected = "CustomerVo [customerName=Bob, customerAddress=321 Oak St"
                + ", principalAmount=1000.0, rate=5.0, time=2.0"
                + ", rateofInterstAmount=100.0, totalAmount=1100.0]";
        assertEquals(expected, vo.toString());
    }
}
