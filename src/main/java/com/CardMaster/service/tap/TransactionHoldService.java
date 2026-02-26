package com.CardMaster.service.tap;

import com.CardMaster.dao.tap.TransactionHoldRepository;
import com.CardMaster.dao.tap.TransactionRepository;
import com.CardMaster.exceptions.tap.TransactionNotFoundException;
import com.CardMaster.model.tap.Transaction;
import com.CardMaster.model.tap.TransactionHold;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionHoldService {

    private final TransactionHoldRepository holdRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Create a hold explicitly (rare in real flow since authorize creates one).
     * Expects a TransactionHold entity with transactionId (Transaction) set already
     * (your controller maps DTO->Entity using TransactionHoldMapper).
     */
    @Transactional
    public TransactionHold create(TransactionHold hold) {
        if (hold == null) throw new IllegalArgumentException("Hold cannot be null");

        // Ensure transaction exists (defensive)
        Transaction tx = null;
        if (hold.getTransactionId() != null && hold.getTransactionId().getTransactionId() != null) {
            Long txId = hold.getTransactionId().getTransactionId();
            tx = transactionRepository.findById(txId)
                    .orElseThrow(() -> new TransactionNotFoundException(txId));
        } else {
            throw new IllegalArgumentException("transactionId must be provided on hold");
        }

        // Default holdDate if missing
        if (hold.getHoldDate() == null) {
            hold.setHoldDate(LocalDateTime.now());
        }

        hold.setTransactionId(tx);
        return holdRepository.save(hold);
    }

    @Transactional(readOnly = true)
    public TransactionHold getById(Long holdId) {
        return holdRepository.findById(holdId)
                .orElseThrow(() -> new IllegalArgumentException("TransactionHold not found: " + holdId));
    }

    /**
     * Lists all holds for a specific transaction.
     * In-memory filter to keep repository simple. You can later add:
     *   List<TransactionHold> findByTransactionId_TransactionId(Long transactionId);
     */
    @Transactional(readOnly = true)
    public List<TransactionHold> listByTransaction(Long transactionId) {
        List<TransactionHold> all = holdRepository.findAll();
        return all.stream()
                .filter(h -> h.getTransactionId() != null
                        && Objects.equals(h.getTransactionId().getTransactionId(), transactionId))
                .sorted(Comparator.comparing(TransactionHold::getHoldDate))
                .collect(Collectors.toList());
    }

    /**
     * Releases a hold: sets releaseDate to now (if not already set).
     */
    @Transactional
    public TransactionHold release(Long holdId) {
        TransactionHold hold = holdRepository.findById(holdId)
                .orElseThrow(() -> new IllegalArgumentException("TransactionHold not found: " + holdId));

        if (hold.getReleaseDate() == null) {
            hold.setReleaseDate(LocalDateTime.now());
            hold = holdRepository.save(hold);

            // TODO (optional): If you maintain available limit, add credit-back logic here on the related account.
        }

        return hold;
    }
}