package com.CardMaster.service.paa;

import com.CardMaster.dao.cpl.CardProductRepository;
import com.CardMaster.dao.paa.CardApplicationRepository;
import com.CardMaster.dao.paa.CustomerRepository;
import com.CardMaster.dao.paa.DocumentRepository;
import com.CardMaster.dto.paa.CardApplicationDto;
import com.CardMaster.exception.paa.ApplicationNotFoundException;
import com.CardMaster.exception.paa.CustomerNotFoundException;
import com.CardMaster.exceptions.cpl.NotFoundException;
import com.CardMaster.model.cpl.CardProduct;
import com.CardMaster.model.paa.CardApplication;
import com.CardMaster.model.paa.Customer;
import com.CardMaster.model.paa.Document;
import com.CardMaster.security.iam.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardApplicationServiceTest {

    @Mock private CardApplicationRepository applicationRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private CardProductRepository productRepository;
    @Mock private DocumentRepository documentRepository;
    @Mock private JwtUtil jwtUtil;

    @InjectMocks private CardApplicationService service;

    private Customer customer;
    private CardProduct product;
    private CardApplication app;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setCustomerId(1L);

        product = new CardProduct();
        product.setProductId(2L);

        app = new CardApplication();
        app.setApplicationId(5L);
        app.setCustomer(customer);
        app.setProduct(product);
        app.setStatus(CardApplication.CardApplicationStatus.Submitted);
    }

//    @Test
//    void testCreateApplication_Success() {
//        CardApplicationDto dto = new CardApplicationDto();
//        dto.setCustomerId(1L);
//        dto.setProductId(2L);
//
//        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
//        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
//        when(applicationRepository.findByCustomerCustomerId(1L)).thenReturn(List.of());
//        when(productRepository.findById(2L)).thenReturn(Optional.of(product));
//        when(applicationRepository.save(any(CardApplication.class))).thenReturn(app);
//
//        CardApplicationDto result = service.create(dto, "Bearer token");
//        assertEquals(5L, result.getApplicationId());
//    }

    @Test
    void testCreateApplication_CustomerNotFound() {
        CardApplicationDto dto = new CardApplicationDto();
        dto.setCustomerId(99L);

        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class,
                () -> service.create(dto, "Bearer token"));
    }

    @Test
    void testCreateApplication_ProductNotFound() {
        CardApplicationDto dto = new CardApplicationDto();
        dto.setCustomerId(1L);
        dto.setProductId(99L);

        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(applicationRepository.findByCustomerCustomerId(1L)).thenReturn(List.of());
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.create(dto, "Bearer token"));
    }

    @Test
    void testFindById_Success() {
        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(applicationRepository.findById(5L)).thenReturn(Optional.of(app));

        CardApplicationDto result = service.findById(5L, "Bearer token");
        assertEquals(5L, result.getApplicationId());
    }

    @Test
    void testFindById_NotFound() {
        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(applicationRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ApplicationNotFoundException.class,
                () -> service.findById(5L, "Bearer token"));
    }

    @Test
    void testGetAllApplications() {
        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(applicationRepository.findAll()).thenReturn(List.of(app));

        List<CardApplicationDto> result = service.getAllApplications("Bearer token");
        assertEquals(1, result.size());
    }

    @Test
    void testGetApplicationsByCustomer() {
        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(applicationRepository.findByCustomerCustomerId(1L)).thenReturn(List.of(app));

        List<CardApplicationDto> result = service.getApplicationsByCustomer(1L, "Bearer token");
        assertEquals(1, result.size());
    }

//    @Test
//    void testUpdateApplicationStatus_Approved() {
//        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
//        when(applicationRepository.findById(5L)).thenReturn(Optional.of(app));
//        when(documentRepository.findByApplicationApplicationId(5L)).thenReturn(List.of());
//        when(applicationRepository.save(any(CardApplication.class))).thenReturn(app);
//
//        CardApplicationDto result = service.updateApplicationStatus(5L, "Approved", "Bearer token");
//        assertEquals("Approved", result.getStatus());
//    }
//
//    @Test
//    void testUpdateApplicationStatus_CascadeRejected() {
//        Document doc = new Document();
//        doc.setStatus(Document.DocumentStatus.Rejected);
//
//        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
//        when(applicationRepository.findById(5L)).thenReturn(Optional.of(app));
//        when(documentRepository.findByApplicationApplicationId(5L)).thenReturn(List.of(doc));
//        when(applicationRepository.save(any(CardApplication.class))).thenReturn(app);
//
//        CardApplicationDto result = service.updateApplicationStatus(5L, "Approved", "Bearer token");
//        assertEquals("Rejected", result.getStatus());
//    }

    @Test
    void testUpdateApplicationStatus_Invalid() {
        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(applicationRepository.findById(5L)).thenReturn(Optional.of(app));

        assertThrows(ApplicationNotFoundException.class,
                () -> service.updateApplicationStatus(5L, "INVALID", "Bearer token"));
    }

    @Test
    void testDeleteApplication_Success() {
        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(applicationRepository.existsById(5L)).thenReturn(true);

        service.deleteApplication(5L, "Bearer token");
        verify(applicationRepository, times(1)).deleteById(5L);
    }

    @Test
    void testDeleteApplication_NotFound() {
        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(applicationRepository.existsById(5L)).thenReturn(false);

        assertThrows(ApplicationNotFoundException.class,
                () -> service.deleteApplication(5L, "Bearer token"));
    }
}
