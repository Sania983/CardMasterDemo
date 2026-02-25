package com.CardMaster.mapper.tap;

import com.CardMaster.dto.transactions.TransactionHoldRequestDto;
import com.CardMaster.dto.transactions.TransactionHoldResponseDto;
import com.CardMaster.model.transactions.TransactionHold;
import org.springframework.stereotype.Component;

@Component
public class TransactionHoldMapper {

    public TransactionHold toEntity(TransactionHoldRequestDto dto) {
        TransactionHold hold = new TransactionHold();
        hold.setTransactionId(dto.getTransactionId());
        hold.setAmount(dto.getAmount());
        hold.setHoldDate(dto.getHoldDate());
        hold.setReleaseDate(dto.getReleaseDate());
        return hold;
    }

    public TransactionHoldResponseDto toDTO(TransactionHold hold) {
        TransactionHoldResponseDto dto = new TransactionHoldResponseDto();
        dto.setHoldId(hold.getHoldId());
        dto.setTransactionId(hold.getTransactionId());
        dto.setAmount(hold.getAmount());
        dto.setHoldDate(hold.getHoldDate());
        dto.setReleaseDate(hold.getReleaseDate());
        return dto;
    }
}
