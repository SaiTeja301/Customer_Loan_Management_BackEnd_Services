package com.SprCustomers.Dao;

import com.SprCustomers.Bo.CustomerBo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.jpa.properties.hibernate.hbm2ddl.create_namespaces=true"
})
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testSaveAndFindCustomer() {
        CustomerBo customer = new CustomerBo();
        customer.setCustomerName("Test User");
        customer.setCustomerAddress("123 Test St");
        customer.setInterestRate(5.0f);
        customer.setPrincipalAmount(1000f);
        customer.setTime(2.0f);

        CustomerBo savedCustomer = customerRepository.save(customer);

        assertNotNull(savedCustomer.getCustomerId());

        CustomerBo foundCustomer = customerRepository.findById(savedCustomer.getCustomerId()).orElse(null);
        assertNotNull(foundCustomer);
        assertEquals("Test User", foundCustomer.getCustomerName());
    }

    @Test
    void testDeleteByCustomerId() {
        CustomerBo customer = new CustomerBo();
        customer.setCustomerName("Alice in Wonderland");
        customer.setCustomerAddress("456 Rabbit Hole");
        customer.setInterestRate(2.0f);
        customer.setPrincipalAmount(200f);
        customer.setTime(1.0f);

        CustomerBo savedCustomer = entityManager.persist(customer);
        entityManager.flush();

        Integer id = savedCustomer.getCustomerId();

        // Ensure it exists
        assertTrue(customerRepository.findById(id).isPresent());

        // Delete
        int deletedCount = customerRepository.deleteByCustomerId(id);
        customerRepository.flush();
        entityManager.clear();

        // Assert
        assertEquals(1, deletedCount, "Should have deleted 1 record");
        assertFalse(customerRepository.findById(id).isPresent());
    }
}
