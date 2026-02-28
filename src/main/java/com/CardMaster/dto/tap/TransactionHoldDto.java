package com.CardMaster.dto.tap;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHoldDto {
    private Long holdId;
    private Long transactionId;
    private Double amount;
    private LocalDateTime holdDate;
    private LocalDateTime releaseDate;
}
<<<<<<< HEAD
=======

>>>>>>> f62a880ef216b474853d19d91cbe8eadacad758b
