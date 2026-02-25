package com.CardMaster.dto.bsp;

import com.CardMaster.model.billing.PaymentMethod;
import com.CardMaster.model.billing.PaymentStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PaymentRequestDto {
    private Long accountId;
    private Double amount;
    private LocalDateTime paymentDate;
    private PaymentMethod method;
    private PaymentStatus status;
}

@Data
public class PaymentResponseDto {
    private Long paymentId;
    private Long accountId;
    private Double amount;
    private LocalDateTime paymentDate;
    private PaymentMethod method;
    private PaymentStatus status;
}
