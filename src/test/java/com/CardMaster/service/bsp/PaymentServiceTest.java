package com.CardMaster.service.bsp;

import com.CardMaster.model.bsp.Payment;
import com.CardMaster.model.cias.CardAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTest {

    @InjectMocks
    private PaymentService service;

    private Payment payment;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Create a CardAccount entity stub
        CardAccount account = new CardAccount();
        account.setAccountId(100L);

        // Create a Payment entity stub
        payment = new Payment();
        payment.setPaymentId(1L);
        payment.setAccountId(account); // ✅ assign entity, not a long
        payment.setAmount(1000.0);
        payment.setPaymentDate(LocalDateTime.now());
    }

    @Test
    void testCapturePaymentThrowsUnsupported() {
        assertThrows(UnsupportedOperationException.class,
                () -> service.capturePayment(payment));
    }

    @Test
    void testGetByIdThrowsUnsupported() {
        assertThrows(UnsupportedOperationException.class,
                () -> service.getById(1L));
    }

    @Test
    void testListAllThrowsUnsupported() {
        assertThrows(UnsupportedOperationException.class,
                () -> service.listAll());
    }
}
