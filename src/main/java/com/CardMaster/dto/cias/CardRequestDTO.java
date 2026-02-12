package com.CardMaster.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class CardRequestDTO {
    private Long customerId;
    private Long productId;
    private String maskedCardNumber;
    private LocalDate expiryDate;
    private String cvvHash;
    private String status; // "ISSUED", "ACTIVE", "BLOCKED"

    // getters and setters
}