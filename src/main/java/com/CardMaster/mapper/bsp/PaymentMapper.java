package com.CardMaster.mapper.bsp;

import com.CardMaster.dto.bsp.PaymentDto;
import com.CardMaster.model.bsp.Payment;
import com.CardMaster.Enum.bsp.PaymentMethod;
import com.CardMaster.Enum.tap.PaymentStatus;
import com.CardMaster.model.cias.CardAccount;
import com.CardMaster.dao.cias.CardAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentMapper {

    private final CardAccountRepository accountRepository;

    // DTO → Entity
    public Payment toEntity(PaymentDto dto) {
        CardAccount account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + dto.getAccountId()));

        Payment p = new Payment();
        p.setAccountId(account);
        p.setAmount(dto.getAmount());
        p.setPaymentDate(dto.getPaymentDate());
        p.setMethod(PaymentMethod.valueOf(dto.getMethod().name()));
        p.setStatus(PaymentStatus.valueOf(dto.getStatus().name()));
        return p;
    }

    // Entity → DTO
    public PaymentDto toDTO(Payment p) {
        PaymentDto dto = new PaymentDto();
        dto.setPaymentId(p.getPaymentId());
        dto.setAccountId(p.getAccountId().getAccountId()); // extract ID from CardAccount
        dto.setAmount(p.getAmount());
        dto.setPaymentDate(p.getPaymentDate());
        dto.setMethod(p.getMethod());
        dto.setStatus(p.getStatus());
        return dto;
    }
}
