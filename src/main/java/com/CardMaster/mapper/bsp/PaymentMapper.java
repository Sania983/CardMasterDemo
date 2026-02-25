package com.CardMaster.mapper.billing;

import com.CardMaster.dto.billing.PaymentRequestDto;
import com.CardMaster.dto.billing.PaymentResponseDto;
import com.CardMaster.model.billing.Payment;
import com.CardMaster.model.billing.PaymentMethod;
import com.CardMaster.model.billing.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public Payment toEntity(PaymentRequestDto dto) {
        Payment p = new Payment();
        p.setAccountId(dto.getAccountId());
        p.setAmount(dto.getAmount());
        p.setPaymentDate(dto.getPaymentDate());
        p.setMethod(PaymentMethod.valueOf(dto.getMethod().name()));
        p.setStatus(PaymentStatus.valueOf(dto.getStatus().name()));
        return p;
    }

    public PaymentResponseDto toDTO(Payment p) {
        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setPaymentId(p.getPaymentId());
        dto.setAccountId(p.getAccountId());
        dto.setAmount(p.getAmount());
        dto.setPaymentDate(p.getPaymentDate());
        dto.setMethod(p.getMethod());
        dto.setStatus(p.getStatus());
        return dto;
    }
}
