package com.SprCustomers.Controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SprCustomers.Dto.AiRequest;
import com.SprCustomers.Dto.AiResponse;
import com.SprCustomers.Dto.CustomerDto;
import com.SprCustomers.Dto.CustomerUpdateRequest;
import com.SprCustomers.Service.CustomerService;
import com.SprCustomers.Service.GeminiAiService;
import com.SprCustomers.Vo.CustomerVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/Spr/customers")
@CrossOrigin
@Tag(name = "Customer Management", description = "APIs for managing customer records and AI assistance")
public class CustomerController {

	private final CustomerService service;
	private final GeminiAiService geminiAiService;

	// ✅ Constructor Injection (Best Practice)
	public CustomerController(CustomerService service, GeminiAiService geminiAiService) {
		this.service = service;
		this.geminiAiService = geminiAiService;
	}

	// ================= AI AGENT ====================
	@PostMapping("/askAgent")
	@Operation(summary = "Ask AI Agent", description = "Send a question to the Gemini AI agent and receive an intelligent response")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully received AI response"),
			@ApiResponse(responseCode = "400", description = "Invalid request - question is required"),
			@ApiResponse(responseCode = "500", description = "AI service error or GOOGLE_API_KEY not configured")
	})
	public ResponseEntity<AiResponse> askAgent(
			@Valid @RequestBody @Parameter(description = "Question to ask the AI agent") AiRequest request) {
		try {
			String answer = geminiAiService.askQuestion(request.getQuestion());
			AiResponse response = new AiResponse(answer);
			return ResponseEntity.ok(response);
		} catch (IllegalStateException e) {
			AiResponse errorResponse = new AiResponse("❌ Error: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		} catch (Exception e) {
			AiResponse errorResponse = new AiResponse("❌ Error communicating with AI service: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	// ================= GET BY ID ====================
	@GetMapping("/getCustomerById")
	@Operation(summary = "Get Customer by ID", description = "Retrieve detailed information about a specific customer including calculated interest and total amount")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Customer found"),
			@ApiResponse(responseCode = "404", description = "Customer not found")
	})
	public ResponseEntity<CustomerVo> getCustomerDetailsById(
			@RequestParam @Parameter(description = "Customer ID") int id) {
		CustomerDto dto = service.getCustomerDetailsById(id);
		if (dto == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		CustomerVo vo = new CustomerVo();
		vo.setCustomerId(String.valueOf(dto.getCustomerId()));
		vo.setCustomerName(dto.getCustomerName());
		vo.setCustomerAddress(dto.getCustomerAddress());
		vo.setPrincipalAmount(String.valueOf(dto.getPrincipalAmount()));
		vo.setRate(String.valueOf(dto.getRate()));
		vo.setTime(String.valueOf(dto.getTime()));
		vo.setRateofInterstAmount(String.valueOf(dto.getRateofInterstAmount()));
		vo.setTotalAmount(String.valueOf(dto.getTotalAmount()));
		return ResponseEntity.status(HttpStatus.OK).body(vo);
	}

	// ================= GET ALL ====================
	@GetMapping("/getAllCustomers")
	@Operation(summary = "Get All Customers", description = "Retrieve a list of all customers with their details")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Customers retrieved successfully"),
			@ApiResponse(responseCode = "204", description = "No customers found")
	})
	public ResponseEntity<List<CustomerVo>> getAllCustomerDetails() {
		List<CustomerDto> dtolist = service.getAllCustomerDetails();
		if (dtolist == null || dtolist.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
		}

		List<CustomerVo> listVo = dtolist.stream().map(dto -> {
			CustomerVo vo = new CustomerVo();
			vo.setCustomerId(String.valueOf(dto.getCustomerId()));
			vo.setCustomerName(dto.getCustomerName());
			vo.setCustomerAddress(dto.getCustomerAddress());
			vo.setPrincipalAmount(String.valueOf(dto.getPrincipalAmount()));
			vo.setRate(String.valueOf(dto.getRate()));
			vo.setTime(String.valueOf(dto.getTime()));
			vo.setRateofInterstAmount(String.valueOf(dto.getRateofInterstAmount()));
			vo.setTotalAmount(String.valueOf(dto.getTotalAmount()));
			return vo;
		}).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(listVo);
	}

	// ================= DELETE ====================
	@DeleteMapping("/deleteCustomerById/{id}")
	@Operation(summary = "Delete Customer", description = "Delete a customer by ID and return the deleted customer's details")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Customer deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Customer not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	public ResponseEntity<String> deleteCustomerById(
			@PathVariable @Parameter(description = "Customer ID to delete") Integer id) {
		try {
			CustomerDto existingCustomer = service.getCustomerDetailsById(id);
			if (existingCustomer == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("❌ Customer not found with ID: " + id);
			}
			service.deleteCustomerById(id);
			return ResponseEntity.status(HttpStatus.OK)
					.body("✅ Customer Details before Deletion:\n"
							+ "Customer ID: " + existingCustomer.getCustomerId() + "\n"
							+ "Name: " + existingCustomer.getCustomerName() + "\n"
							+ "Address: " + existingCustomer.getCustomerAddress() + "\n"
							+ "Principal Amount: " + existingCustomer.getPrincipalAmount() + "\n"
							+ "Rate: " + existingCustomer.getRate() + "\n"
							+ "Time: " + existingCustomer.getTime() + "\n"
							+ "Interest Amount: " + existingCustomer.getRateofInterstAmount() + "\n"
							+ "Total Amount: " + existingCustomer.getTotalAmount() + "\n"
							+ "Customer Deleted Successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("❌ Error while deleting customer: " + e.getMessage());
		}
	}

	// ================= UPDATE ====================
	@PutMapping("/updateCustomerById")
	@Operation(summary = "Update Customer", description = "Update customer information. All fields are optional for partial updates.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Customer updated successfully"),
			@ApiResponse(responseCode = "404", description = "Customer not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	public ResponseEntity<String> updateCustomerById(
			@RequestParam @Parameter(description = "Customer ID") int id,
			@Valid @RequestBody @Parameter(description = "Fields to update") CustomerUpdateRequest updateRequest) {
		try {
			CustomerDto updatedCustomer = service.updateCustomerById(id, updateRequest);

			if (updatedCustomer == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("❌ Customer with ID " + id + " not found.");
			}

			String response = String.format("""
					✅ Customer Updated Successfully:
					--------------------------------
					ID: %d
					Name: %s
					Address: %s
					Principal Amount: %.2f
					Interest Rate: %.2f
					Time: %.2f
					Interest Amount: %.2f
					Total Amount: %.2f
					""",
					updatedCustomer.getCustomerId(),
					updatedCustomer.getCustomerName(),
					updatedCustomer.getCustomerAddress(),
					updatedCustomer.getPrincipalAmount(),
					updatedCustomer.getRate(),
					updatedCustomer.getTime(),
					updatedCustomer.getRateofInterstAmount(),
					updatedCustomer.getTotalAmount());

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("❌ Error while updating customer: " + e.getMessage());
		}
	}

	// ================= INSERT ====================
	@PostMapping("/insertCustomer")
	@Operation(summary = "Create Customer", description = "Create a new customer record. Interest and total amount are calculated automatically.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Customer created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid customer data"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	public ResponseEntity<String> insertCustomer(
			@Valid @RequestBody @Parameter(description = "Customer details") CustomerDto customerDto) {
		try {
			CustomerDto dto = service.insertCustomer(customerDto);
			if (dto == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ Invalid customer data provided.");
			}
			String response = String.format("""
					✅ Customer Inserted Successfully:
					--------------------------------
					ID: %d
					Name: %s
					Address: %s
					Principal Amount: %.2f
					Interest Rate: %.2f
					Time: %.2f
					Interest Amount: %.2f
					Total Amount: %.2f
					""",
					dto.getCustomerId(),
					dto.getCustomerName(),
					dto.getCustomerAddress(),
					dto.getPrincipalAmount(),
					dto.getRate(),
					dto.getTime(),
					dto.getRateofInterstAmount(),
					dto.getTotalAmount());
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("❌ Error while inserting customer: " + e.getMessage());
		}
	}
}
