package com.SprCustomers.Bo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Spring_Customers", schema = "testschema")
public class CustomerBo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CID")
	private Integer customerId;

	@Column(name = "Customer_Name")
	private String customerName;

	@Column(name = "Customer_Address")
	private String customerAddress;

	@Column(name = "`Interest_Rate`")
	private Float interestRate;

	@Column(name = "`Principle_Amount`")
	private Float principalAmount;

	@Column(name = "Time")
	private Float time;

	// @Transient - When you have a field in your entity class that you want to use
	// in the application logic but not save in the database, you mark it with
	// @Transient
	@Transient
	private Float rateofInterstAmount;

	@Transient
	private Float totalAmount;

	@Override
	public String toString() {
		return "CustomerBo [customerId=" + customerId + ", customerName=" + customerName + ", customerAddress="
				+ customerAddress + ", principalAmount=" + principalAmount + ", rate=" + interestRate + ", time=" + time
				+ ", rateofInterstAmount=" + rateofInterstAmount + ", totalAmount=" + totalAmount + "]";
	}

}
