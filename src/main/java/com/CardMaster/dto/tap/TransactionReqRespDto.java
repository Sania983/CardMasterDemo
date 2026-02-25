package com.CardMaster.dto.tap;

import com.CardMaster.model.transactions.Channel;
import com.CardMaster.model.transactions.TransactionStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionRequestDto {
    private Long accountId;
    private Double amount;
    private String currency;
    private String merchant;
    private Channel channel;
    private LocalDateTime transactionDate;
    private TransactionStatus status;
}

@Data
public class TransactionResponseDto {
    private Long transactionId;
    private Long accountId;
    private Double amount;
    private String currency;
    private String merchant;
    private Channel channel;
    private LocalDateTime transactionDate;
    private TransactionStatus status;
}
