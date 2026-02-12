package com.CardMaster.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
@Data
public class CardAccountRequestDTO {
    private Long cardId;

    @NotNull(message = "Credit limit is required")
    @Positive(message = "Credit limit must be positive")
    private Double creditLimit;
    private Double availableLimit;
    private String status; // "ACTIVE", "CLOSED"

    // getters and setters
}



