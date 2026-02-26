package com.CardMaster.service.paa;

import com.CardMaster.dao.paa.CustomerRepository;
import com.CardMaster.dto.paa.CustomerDto;
import com.CardMaster.exceptions.paa.CustomerNotFoundException;
import com.CardMaster.mapper.paa.EntityMapper;
import com.CardMaster.model.paa.ContactInfo_Customer;
import com.CardMaster.model.paa.Customer;
import com.CardMaster.security.iam.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // --- Create Customer ---
    @Test
    void testCreateCustomer_Success() {
        CustomerDto dto = new CustomerDto();
        dto.setName("Alice");
        dto.setDob("1990-01-01");
        dto.setIncome(50000.0);
        dto.setEmploymentType("Salaried");


        dto.setStatus("Active");

        dto.setContactInfo(new ContactInfo_Customer("123 Street", "alice@example.com", "9876543210"));

        Customer entity = EntityMapper.toCustomerEntity(dto);
        entity.setCustomerId(1L);

        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(customerRepository.save(any(Customer.class))).thenReturn(entity);

        CustomerDto result = customerService.createCustomer(dto, "Bearer token");
        assertEquals("Alice", result.getName());
        assertEquals("alice@example.com", result.getContactInfo().getEmail());
        assertEquals(1L, result.getCustomerId());
    }

    // --- Get Customer by ID ---
    @Test
    void testGetCustomer_Success() {
        ContactInfo_Customer contactInfo = new ContactInfo_Customer("123 Street", "bob@example.com", "1234567890");
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("Bob");
        customer.setDob(LocalDate.of(1985, 5, 20));
        customer.setEmploymentType(Customer.EmploymentType.Salaried);
        customer.setStatus(Customer.CustomerStatus.Active);
        customer.setContactInfo(contactInfo);

        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerDto result = customerService.getCustomer(1L, "Bearer token");
        assertEquals("Bob", result.getName());
        assertEquals("bob@example.com", result.getContactInfo().getEmail());
    }


    @Test
    void testGetCustomer_NotFound() {
        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class,
                () -> customerService.getCustomer(1L, "Bearer token"));
    }

    // --- Update Customer ---
    @Test
    void testUpdateCustomer_Success() {
        Customer existing = new Customer();
        existing.setCustomerId(1L);
        existing.setName("Old Name");
        existing.setDob(LocalDate.of(1985, 5, 20));
        existing.setContactInfo(new ContactInfo_Customer("Old Address", "old@example.com", "1111111111"));

        CustomerDto dto = new CustomerDto();
        dto.setName("New Name");
        dto.setDob("1995-05-05");
        dto.setIncome(60000.0);
        dto.setEmploymentType("Salaried");
        dto.setStatus("Active");
        dto.setContactInfo(new ContactInfo_Customer("New Address", "new@example.com", "2222222222"));

        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(customerRepository.save(any(Customer.class))).thenReturn(existing);

        CustomerDto result = customerService.updateCustomer(1L, dto, "Bearer token");
        assertEquals("New Name", result.getName());
        assertEquals("new@example.com", result.getContactInfo().getEmail());
        assertEquals("New Address", result.getContactInfo().getAddress());
    }

    // --- Delete Customer ---
    @Test
    void testDeleteCustomer_Success() {
        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(customerRepository.existsById(1L)).thenReturn(true);

        customerService.deleteCustomer(1L, "Bearer token");
        verify(customerRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCustomer_NotFound() {
        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(customerRepository.existsById(1L)).thenReturn(false);

        assertThrows(CustomerNotFoundException.class,
                () -> customerService.deleteCustomer(1L, "Bearer token"));
    }

    // --- Get All Customers ---
    @Test
    void testGetAllCustomers_Success() {
        Customer customer1 = new Customer();
        customer1.setCustomerId(1L);
        customer1.setName("Alice");
        customer1.setDob(LocalDate.of(1990, 1, 1));
        customer1.setEmploymentType(Customer.EmploymentType.Salaried);
        customer1.setStatus(Customer.CustomerStatus.Active);
        customer1.setContactInfo(new ContactInfo_Customer("Addr1", "alice@example.com", "1111111111"));

        Customer customer2 = new Customer();
        customer2.setCustomerId(2L);
        customer2.setName("Bob");
        customer2.setDob(LocalDate.of(1985, 5, 20));
        customer2.setEmploymentType(Customer.EmploymentType.Student);
        customer2.setStatus(Customer.CustomerStatus.Inactive);
        customer2.setContactInfo(new ContactInfo_Customer("Addr2", "bob@example.com", "2222222222"));

        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(customerRepository.findAll()).thenReturn(List.of(customer1, customer2));

        List<CustomerDto> result = customerService.getAllCustomers("Bearer token");
        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
        assertEquals("bob@example.com", result.get(1).getContactInfo().getEmail());
    }

}
