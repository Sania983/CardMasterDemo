package com.CardMaster.mapper.tap;

import com.CardMaster.dto.tap.TransactionDto;
import com.CardMaster.model.tap.Transaction;
import com.CardMaster.model.cias.CardAccount;
import com.CardMaster.dao.cias.CardAccountRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TransactionMapper {

    private final CardAccountRepository accountRepository;

    // DTO → Entity
    public Transaction toEntity(TransactionDto dto) {
        CardAccount account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + dto.getAccountId()));

        Transaction tx = new Transaction();
        tx.setAccountId(account);
        tx.setAmount(dto.getAmount());
        tx.setCurrency(dto.getCurrency());
        tx.setMerchant(dto.getMerchant());
        tx.setChannel(dto.getChannel());
        tx.setTransactionDate(dto.getTransactionDate());
        tx.setStatus(dto.getStatus());
        return tx;
    }

    // Entity → DTO
    public TransactionDto toDTO(Transaction tx) {
        TransactionDto dto = new TransactionDto();
        dto.setTransactionId(tx.getTransactionId());
        dto.setAccountId(tx.getAccountId().getAccountId()); // extract ID from CardAccount
        dto.setAmount(tx.getAmount());
        dto.setCurrency(tx.getCurrency());
        dto.setMerchant(tx.getMerchant());
        dto.setChannel(tx.getChannel());
        dto.setTransactionDate(tx.getTransactionDate());
        dto.setStatus(tx.getStatus());
        return dto;
    }

}
