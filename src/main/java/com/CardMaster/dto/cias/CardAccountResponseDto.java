package com.CardMaster.dto.cias;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AccountResponseDTO {
    private Long accountId;
    private Long cardId;
    private Double creditLimit;
    private Double availableLimit;
    private LocalDate openDate;
    private String status;
}
