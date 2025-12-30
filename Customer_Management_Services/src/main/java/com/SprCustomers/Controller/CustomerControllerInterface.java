package com.SprCustomers.Controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import com.SprCustomers.Dto.CustomerDto;
import com.SprCustomers.Dto.CustomerUpdateRequest;
import com.SprCustomers.Vo.CustomerVo;

public interface CustomerControllerInterface {
	public ResponseEntity<CustomerVo> getCustomerDetailsById(int id);

	public ResponseEntity<List<CustomerVo>> getAllCustomerDetails();

	public ResponseEntity<String> deleteCustomerById(Integer id);

	public ResponseEntity<String> updateCustomerById(int id, CustomerUpdateRequest updateRequest);

	public ResponseEntity<String> insertCustomer(CustomerDto customerDto);
}
