package com.SprCustomers.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.SprCustomers.Bo.CustomerBo;
import com.SprCustomers.Dao.CustomerRepository;
import com.SprCustomers.Dto.CustomerDto;
import com.SprCustomers.Dto.CustomerUpdateRequest;

@Service
public class customerServiceImpl implements CustomerService {

	private final ModelMapper modelMapper;
	private final CustomerRepository customerRepository;

	public customerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper) {
		this.customerRepository = customerRepository;
		this.modelMapper = modelMapper;
	}

	@Transactional(readOnly = true)
	@Override
	public CustomerDto getCustomerDetailsById(Integer id) {
		return customerRepository.findById(id).map(this::toDto).orElse(null);
	}

	@Transactional(readOnly = true)
	@Override
	public List<CustomerDto> getAllCustomerDetails() {
		return customerRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
	}

	// DELETE must have write transaction enabled with Custom Method
	@Transactional
	@Override
	public int deleteCustomerById(Integer id) {
		int count = customerRepository.deleteByCustomerId(id);
		if (count > 0) {
			System.out.println("Customer with ID " + id + " deleted successfully....." + count);
			return 1;
		}
		return 0;
	}

	// DELETE with JPA Repository Method
	@Transactional
	@Override
	public void deleteById(Integer id) {
		CustomerBo customer = customerRepository.findById(id).orElse(null);
		if (customer == null) {
			System.out.println("Customer with ID " + id + " not found.");
			return;
		}
		customerRepository.delete(customer);
		System.out.println("Customer with ID " + id + " deleted successfully.....");
	}

	// UPDATE
	@Override
	public CustomerDto updateCustomerById(Integer id, CustomerUpdateRequest updateRequest) {
		Optional<CustomerBo> optionalCustomer = customerRepository.findById(id);

		if (optionalCustomer.isEmpty()) {
			return null;
		}

		CustomerBo existingCustomer = optionalCustomer.get();

		// Update only fields that are not null
		if (updateRequest.getCustomerName() != null)
			existingCustomer.setCustomerName(updateRequest.getCustomerName());

		if (updateRequest.getCustomerAddress() != null)
			existingCustomer.setCustomerAddress(updateRequest.getCustomerAddress());

		if (updateRequest.getPrincipalAmount() != null)
			existingCustomer.setPrincipalAmount(updateRequest.getPrincipalAmount());

		if (updateRequest.getInterestRate() != null)
			existingCustomer.setInterestRate(updateRequest.getInterestRate());

		if (updateRequest.getTime() != null)
			existingCustomer.setTime(updateRequest.getTime());

		CustomerBo savedCustomer = customerRepository.save(existingCustomer);
		return toDto(savedCustomer);
	}

	@Override
	public CustomerDto insertCustomer(CustomerDto customerDto) {
		if (customerDto == null) {
			throw new IllegalArgumentException("❌ Customer data cannot be null");
		}

		// Convert DTO to Entity
		CustomerBo bo = new CustomerBo();
		bo.setCustomerName(customerDto.getCustomerName());
		bo.setCustomerAddress(customerDto.getCustomerAddress());
		bo.setPrincipalAmount(customerDto.getPrincipalAmount());
		bo.setInterestRate(customerDto.getRate());
		bo.setTime(customerDto.getTime());

		// Save and Convert Back
		CustomerBo savedBo = customerRepository.save(bo);
		return toDto(savedBo);
	}

	/**
	 * Convert Business Object (BO) to Data Transfer Object (DTO)
	 * Maps entity to DTO and calculates derived fields (interest and total)
	 */
	private CustomerDto toDto(CustomerBo bo) {
		CustomerDto dto = modelMapper.map(bo, CustomerDto.class);
		// Simple Interest = (P * R * T) / 100
		dto.setRateofInterstAmount((bo.getPrincipalAmount() * bo.getInterestRate() * bo.getTime()) / 100);
		// Total Amount = Principal + Interest
		dto.setTotalAmount(dto.getPrincipalAmount() + dto.getRateofInterstAmount());
		return dto;
	}

}
