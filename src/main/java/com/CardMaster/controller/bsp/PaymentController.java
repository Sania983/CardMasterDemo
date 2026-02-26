package com.CardMaster.controller.bsp;

import com.CardMaster.dto.bsp.PaymentDto;
import com.CardMaster.mapper.bsp.PaymentMapper;
import com.CardMaster.model.bsp.Payment;
import com.CardMaster.service.bsp.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billing/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;
    private final PaymentMapper mapper;

    /**
     * Capture (record) a payment for an account.
     * The service should update statement dues and account balances accordingly.
     */
    @PostMapping("/capture")
    public ResponseEntity<PaymentDto> capture(@RequestBody PaymentDto dto) {
        Payment input = mapper.toEntity(dto);
        Payment saved = service.capturePayment(input);
        return ResponseEntity.ok(mapper.toDTO(saved));
    }

    /**
     * Get a payment by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> get(@PathVariable Long id) {
        Payment p = service.getById(id);
        return ResponseEntity.ok(mapper.toDTO(p));
    }

    /**
     * List all payments.
     */
    @GetMapping
    public ResponseEntity<List<PaymentDto>> list() {
        List<Payment> list = service.listAll();
        return ResponseEntity.ok(list.stream().map(mapper::toDTO).toList());
    }
}