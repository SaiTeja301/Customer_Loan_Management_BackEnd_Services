package com.SprCustomers.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for Customer
 * Used for transferring customer data between layers
 */
@Setter
@Getter
public class CustomerDto {

	private Integer customerId;

	@NotBlank(message = "Customer name is required")
	private String customerName;

	@NotBlank(message = "Customer address is required")
	private String customerAddress;

	@NotNull(message = "Principal amount is required")
	@Positive(message = "Principal amount must be positive")
	private Float principalAmount;

	@NotNull(message = "Interest rate is required")
	@Positive(message = "Interest rate must be positive")
	private Float rate;

	@NotNull(message = "Time period is required")
	@Positive(message = "Time period must be positive")
	private Float time;

	private Float rateofInterstAmount;
	private Float totalAmount;

	public CustomerDto() {
	}

	@Override
	public String toString() {
		return "CustomerDto [customerId=" + customerId + ", customerName=" + customerName + ", customerAddress="
				+ customerAddress + ", principalAmount=" + principalAmount + ", rate=" + rate + ", time=" + time
				+ ", rateofInterstAmount=" + rateofInterstAmount + ", totalAmount=" + totalAmount + "]";
	}
}
