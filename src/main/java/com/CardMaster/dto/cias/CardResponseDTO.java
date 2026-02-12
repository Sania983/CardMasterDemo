package com.CardMaster.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class CardResponseDTO {
    private Long cardId;
    private Long customerId;
    private Long productId;
    private String maskedCardNumber;
    private LocalDate expiryDate;
    private String status;

    // getters and setters
}
