package com.SprCustomers.Vo;

import lombok.Getter;
import lombok.Setter;

/**
 * View Object for Customer
 * Used for presenting customer data to the client
 * All fields are strings for easy display formatting
 */
@Getter
@Setter
public class CustomerVo {
	private String customerId;
	private String customerName;
	private String customerAddress;
	private String principalAmount;
	private String rate;
	private String time;
	private String rateofInterstAmount;
	private String totalAmount;

	public CustomerVo() {
	}

	@Override
	public String toString() {
		return "CustomerVo [customerName=" + customerName + ", customerAddress=" + customerAddress
				+ ", principalAmount=" + principalAmount + ", rate=" + rate + ", time=" + time
				+ ", rateofInterstAmount=" + rateofInterstAmount + ", totalAmount=" + totalAmount + "]";
	}
}
