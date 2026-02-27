package com.CardMaster.service.tap;

import com.CardMaster.dao.tap.TransactionRepository;
import com.CardMaster.dao.tap.TransactionHoldRepository;
import com.CardMaster.Enum.tap.TransactionStatus;
import com.CardMaster.exceptions.tap.TransactionNotFoundException;
import com.CardMaster.model.tap.Transaction;
import com.CardMaster.model.tap.TransactionHold;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepo;
    private final TransactionHoldRepository holdRepo;

    /**
     * AUTHORIZE a transaction (create a hold + set status).
     */
    @Transactional
    public Transaction authorize(Transaction tx) {

        tx.setStatus(TransactionStatus.AUTHORIZED);
        tx.setTransactionDate(LocalDateTime.now());

        // Save transaction
        Transaction saved = transactionRepo.save(tx);

        // Create 1 hold for this transaction
        TransactionHold hold = new TransactionHold();
        hold.setTransactionId(saved);
        hold.setAmount(saved.getAmount());
        hold.setHoldDate(LocalDateTime.now());
        holdRepo.save(hold);

        return saved;
    }

    /**
     * POST (capture) an AUTHORIZED transaction.
     */
    @Transactional
    public com.CardMaster.dto.tap.TransactionDto post(Long id) {

        Transaction tx = transactionRepo.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));

        if (tx.getStatus() != TransactionStatus.AUTHORIZED) {
            throw new IllegalStateException("Only AUTHORIZED transactions can be posted");
        }

        // Find the first hold for this transaction
        TransactionHold hold = holdRepo.findAll().stream()
                .filter(h -> h.getTransactionId().getTransactionId().equals(id)
                        && h.getReleaseDate() == null)
                .findFirst()
                .orElse(null);

        // Release hold
        if (hold != null) {
            hold.setReleaseDate(LocalDateTime.now());
            holdRepo.save(hold);
        }

        tx.setStatus(TransactionStatus.POSTED);
        tx = transactionRepo.save(tx);

        // Convert Entity → DTO manually (beginner-friendly)
        com.CardMaster.dto.tap.TransactionDto dto =
                new com.CardMaster.dto.tap.TransactionDto(
                        tx.getTransactionId(),
                        tx.getAccountId().getAccountId(),
                        tx.getAmount(),
                        tx.getCurrency(),
                        tx.getMerchant(),
                        tx.getChannel(),
                        tx.getTransactionDate(),
                        tx.getStatus()
                );
        return dto;
    }

    /**
     * REVERSE a transaction.
     */
    @Transactional
    public Transaction reverse(Long id) {

        Transaction tx = transactionRepo.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));

        // Release hold if exists
        TransactionHold hold = holdRepo.findAll().stream()
                .filter(h -> h.getTransactionId().getTransactionId().equals(id)
                        && h.getReleaseDate() == null)
                .findFirst()
                .orElse(null);

        if (hold != null) {
            hold.setReleaseDate(LocalDateTime.now());
            holdRepo.save(hold);
        }

        tx.setStatus(TransactionStatus.REVERSED);
        return transactionRepo.save(tx);
    }

    /**
     * Get transaction by ID.
     */
    @Transactional(readOnly = true)
    public Transaction getById(Long id) {
        return transactionRepo.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    /**
     * List all transactions.
     */
    @Transactional(readOnly = true)
    public List<Transaction> listAll() {
        return transactionRepo.findAll();
    }
}