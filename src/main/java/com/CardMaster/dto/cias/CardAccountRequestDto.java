package com.CardMaster.dto.cias;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardAccountRequestDto {
    private Long cardId;

    @NotNull(message = "Credit limit is required")
    @Positive(message = "Credit limit must be positive")
    private Double creditLimit;

    private Double availableLimit;
    private String status; // ACTIVE, CLOSED
}
