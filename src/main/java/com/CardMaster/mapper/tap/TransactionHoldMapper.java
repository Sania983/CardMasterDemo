package com.CardMaster.mapper.tap;

import com.CardMaster.dto.tap.TransactionHoldDto;
import com.CardMaster.model.tap.TransactionHold;
import com.CardMaster.model.tap.Transaction;
import com.CardMaster.dao.tap.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TransactionHoldMapper {

    private final TransactionRepository transactionRepository;

    // DTO → Entity
    public TransactionHold toEntity(TransactionHoldDto dto) {
        Transaction transaction = transactionRepository.findById(dto.getTransactionId())
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found with ID: " + dto.getTransactionId()));

        TransactionHold hold = new TransactionHold();
<<<<<<< HEAD

        hold.setTransactionId(transaction);   // matches entity field name

        hold.setTransactionId(transaction);   //  matches entity field name

=======
        hold.setTransactionId(transaction);   //  matches entity field name
>>>>>>> f62a880ef216b474853d19d91cbe8eadacad758b
        hold.setAmount(dto.getAmount());
        hold.setHoldDate(dto.getHoldDate());
        hold.setReleaseDate(dto.getReleaseDate());
        return hold;
    }

    // Entity → DTO
    public TransactionHoldDto toDTO(TransactionHold hold) {
        TransactionHoldDto dto = new TransactionHoldDto();
        dto.setHoldId(hold.getHoldId());
<<<<<<< HEAD

        dto.setTransactionId(hold.getTransactionId().getTransactionId()); // extract ID from Transaction

        dto.setTransactionId(hold.getTransactionId().getTransactionId()); //  extract ID from Transaction

=======
        dto.setTransactionId(hold.getTransactionId().getTransactionId()); //  extract ID from Transaction
>>>>>>> f62a880ef216b474853d19d91cbe8eadacad758b
        dto.setAmount(hold.getAmount());
        dto.setHoldDate(hold.getHoldDate());
        dto.setReleaseDate(hold.getReleaseDate());
        return dto;
    }
}
