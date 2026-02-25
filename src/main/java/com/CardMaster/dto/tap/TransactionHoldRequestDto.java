package com.CardMaster.dto.tap;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TransactionHoldRequestDto {
    private Long transactionId;
    private Double amount;
    private LocalDateTime holdDate;
    private LocalDateTime releaseDate;
}

@Data
public class TransactionHoldResponseDto {
    private Long holdId;
    private Long transactionId;
    private Double amount;
    private LocalDateTime holdDate;
    private LocalDateTime releaseDate;
}
