package com.CardMaster.mapper.tap;

import com.CardMaster.dto.transactions.TransactionRequestDto;
import com.CardMaster.dto.transactions.TransactionResponseDto;
import com.CardMaster.model.transactions.Transaction;
import com.CardMaster.model.transactions.Channel;
import com.CardMaster.model.transactions.TransactionStatus;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public Transaction toEntity(TransactionRequestDto dto) {
        Transaction tx = new Transaction();
        tx.setAccountId(dto.getAccountId());
        tx.setAmount(dto.getAmount());
        tx.setCurrency(dto.getCurrency());
        tx.setMerchant(dto.getMerchant());
        tx.setChannel(Channel.valueOf(dto.getChannel().name()));
        tx.setTransactionDate(dto.getTransactionDate());
        tx.setStatus(TransactionStatus.valueOf(dto.getStatus().name()));
        return tx;
    }

    public TransactionResponseDto toDTO(Transaction tx) {
        TransactionResponseDto dto = new TransactionResponseDto();
        dto.setTransactionId(tx.getTransactionId());
        dto.setAccountId(tx.getAccountId());
        dto.setAmount(tx.getAmount());
        dto.setCurrency(tx.getCurrency());
        dto.setMerchant(tx.getMerchant());
        dto.setChannel(tx.getChannel());
        dto.setTransactionDate(tx.getTransactionDate());
        dto.setStatus(tx.getStatus());
        return dto;
    }
}
