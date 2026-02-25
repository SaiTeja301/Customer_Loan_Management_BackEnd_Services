package com.SprCustomers.Service;

import com.SprCustomers.Bo.CustomerBo;
import com.SprCustomers.Dao.CustomerRepository;
import com.SprCustomers.Dto.CustomerDto;
import com.SprCustomers.Dto.CustomerUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class customerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private customerServiceImpl customerService;

    private CustomerBo sampleBo;
    private CustomerDto sampleDto;

    @BeforeEach
    void setUp() {
        sampleBo = new CustomerBo();
        sampleBo.setCustomerId(1);
        sampleBo.setCustomerName("John Doe");
        sampleBo.setCustomerAddress("123 St");
        sampleBo.setPrincipalAmount(1000f);
        sampleBo.setInterestRate(10f);
        sampleBo.setTime(2f);

        sampleDto = new CustomerDto();
        sampleDto.setCustomerId(1);
        sampleDto.setCustomerName("John Doe");
        sampleDto.setCustomerAddress("123 St");
        sampleDto.setPrincipalAmount(1000f);
        sampleDto.setRate(10f); // Map to interestRate
        sampleDto.setTime(2f);
    }

    @Test
    void testGetCustomerDetailsById_Success() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(sampleBo));
        when(modelMapper.map(sampleBo, CustomerDto.class)).thenReturn(sampleDto);

        CustomerDto result = customerService.getCustomerDetailsById(1);

        assertNotNull(result);
        assertEquals(200f, result.getRateofInterstAmount());
        assertEquals(1200f, result.getTotalAmount());
        assertEquals("John Doe", result.getCustomerName());
    }

    @Test
    void testGetCustomerDetailsById_NotFound() {
        when(customerRepository.findById(99)).thenReturn(Optional.empty());

        CustomerDto result = customerService.getCustomerDetailsById(99);

        assertNull(result);
    }

    @Test
    void testGetAllCustomerDetails() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(sampleBo));
        when(modelMapper.map(sampleBo, CustomerDto.class)).thenReturn(sampleDto);

        List<CustomerDto> result = customerService.getAllCustomerDetails();

        assertEquals(1, result.size());
        assertEquals(200f, result.get(0).getRateofInterstAmount());
    }

    @Test
    void testDeleteCustomerById_Found() {
        when(customerRepository.deleteByCustomerId(1)).thenReturn(1);
        int result = customerService.deleteCustomerById(1);
        assertEquals(1, result);
    }

    @Test
    void testDeleteCustomerById_NotFound() {
        when(customerRepository.deleteByCustomerId(99)).thenReturn(0);
        int result = customerService.deleteCustomerById(99);
        assertEquals(0, result);
    }

    @Test
    void testDeleteById_Found() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(sampleBo));
        doNothing().when(customerRepository).delete(sampleBo);

        customerService.deleteById(1);

        verify(customerRepository, times(1)).delete(sampleBo);
    }

    @Test
    void testDeleteById_NotFound() {
        when(customerRepository.findById(99)).thenReturn(Optional.empty());

        customerService.deleteById(99);

        verify(customerRepository, never()).delete(any());
    }

    @Test
    void testUpdateCustomerById_Success() {
        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setCustomerName("Jane Doe");
        request.setCustomerAddress("456 Ave");
        request.setPrincipalAmount(2000f);
        request.setInterestRate(5f);
        request.setTime(3f);

        when(customerRepository.findById(1)).thenReturn(Optional.of(sampleBo));
        when(customerRepository.save(any(CustomerBo.class))).thenReturn(sampleBo);
        when(modelMapper.map(sampleBo, CustomerDto.class)).thenReturn(sampleDto);

        CustomerDto result = customerService.updateCustomerById(1, request);

        assertNotNull(result);
        assertEquals("Jane Doe", sampleBo.getCustomerName());
        assertEquals("456 Ave", sampleBo.getCustomerAddress());
        assertEquals(2000f, sampleBo.getPrincipalAmount());
        assertEquals(5f, sampleBo.getInterestRate());
        assertEquals(3f, sampleBo.getTime());
    }

    @Test
    void testUpdateCustomerById_PartialUpdate() {
        CustomerUpdateRequest request = new CustomerUpdateRequest();
        // Only update name
        request.setCustomerName("Jane Doe");

        when(customerRepository.findById(1)).thenReturn(Optional.of(sampleBo));
        when(customerRepository.save(any(CustomerBo.class))).thenReturn(sampleBo);
        when(modelMapper.map(sampleBo, CustomerDto.class)).thenReturn(sampleDto);

        CustomerDto result = customerService.updateCustomerById(1, request);

        assertNotNull(result);
        assertEquals("Jane Doe", sampleBo.getCustomerName());
        // Original values remain unchanged
        assertEquals("123 St", sampleBo.getCustomerAddress());
        assertEquals(1000f, sampleBo.getPrincipalAmount());
        assertEquals(10f, sampleBo.getInterestRate());
        assertEquals(2f, sampleBo.getTime());
    }

    @Test
    void testUpdateCustomerById_EmptyUpdate() {
        CustomerUpdateRequest request = new CustomerUpdateRequest();

        when(customerRepository.findById(1)).thenReturn(Optional.of(sampleBo));
        when(customerRepository.save(any(CustomerBo.class))).thenReturn(sampleBo);
        when(modelMapper.map(sampleBo, CustomerDto.class)).thenReturn(sampleDto);

        CustomerDto result = customerService.updateCustomerById(1, request);

        assertNotNull(result);
        assertEquals("John Doe", sampleBo.getCustomerName());
    }

    @Test
    void testUpdateCustomerById_NotFound() {
        when(customerRepository.findById(99)).thenReturn(Optional.empty());

        CustomerDto result = customerService.updateCustomerById(99, new CustomerUpdateRequest());

        assertNull(result);
        verify(customerRepository, never()).save(any());
    }

    @Test
    void testInsertCustomer_Success() {
        when(customerRepository.save(any(CustomerBo.class))).thenReturn(sampleBo);
        when(modelMapper.map(sampleBo, CustomerDto.class)).thenReturn(sampleDto);

        CustomerDto result = customerService.insertCustomer(sampleDto);

        assertNotNull(result);
        verify(customerRepository, times(1)).save(any(CustomerBo.class));
    }

    @Test
    void testInsertCustomer_NullDto() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            customerService.insertCustomer(null);
        });

        assertEquals("❌ Customer data cannot be null", exception.getMessage());
        verify(customerRepository, never()).save(any());
    }
}
