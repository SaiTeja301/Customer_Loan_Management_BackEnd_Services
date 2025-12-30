package com.SprCustomers.Service;
import java.util.List;
import com.SprCustomers.Dto.CustomerDto;
import com.SprCustomers.Dto.CustomerUpdateRequest;

public interface CustomerService {
	public CustomerDto getCustomerDetailsById(Integer id);
	public List<CustomerDto> getAllCustomerDetails();
	public int deleteCustomerById(Integer id);
	public void deleteById(Integer id);
	public CustomerDto updateCustomerById(Integer id,CustomerUpdateRequest updateRequest);
	public CustomerDto insertCustomer(CustomerDto customerDto);
}
