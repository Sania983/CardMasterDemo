package com.CardMaster.service.bsp;

import com.CardMaster.model.bsp.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    /**
     * Capture (record) a payment.
     */
    public Payment capturePayment(Payment payment) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Get a payment by ID.
     */
    public Payment getById(Long id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * List all payments.
     */
    public List<Payment> listAll() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}