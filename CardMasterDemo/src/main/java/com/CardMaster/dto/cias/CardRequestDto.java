package com.CardMaster.dto.cias;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardRequestDto {
    private Long customerId;
    private Long productId;
    private String maskedCardNumber;
    private LocalDate expiryDate;
    private String cvvHash;
    private String status; // ISSUED, ACTIVE, BLOCKED
}
