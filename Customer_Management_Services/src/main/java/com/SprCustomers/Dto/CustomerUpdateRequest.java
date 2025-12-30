package com.SprCustomers.Dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for Customer Update Requests
 * All fields are optional to support partial updates
 */
@Getter
@Setter
public class CustomerUpdateRequest {
    private String customerName;
    private String customerAddress;

    @Positive(message = "Principal amount must be positive")
    private Float principalAmount;

    @Positive(message = "Interest rate must be positive")
    private Float interestRate;

    @Positive(message = "Time period must be positive")
    private Float time;

    @Override
    public String toString() {
        return "CustomerUpdateRequest [customerName=" + customerName + ", customerAddress=" + customerAddress
                + ", principalAmount=" + principalAmount + ", interestRate=" + interestRate + ", time=" + time + "]";
    }

}
