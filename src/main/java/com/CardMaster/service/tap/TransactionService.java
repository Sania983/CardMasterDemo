package com.CardMaster.service.tap;

import com.CardMaster.Enum.tap.TransactionStatus;
import com.CardMaster.dao.tap.TransactionHoldRepository;
import com.CardMaster.dao.tap.TransactionRepository;
import com.CardMaster.dto.tap.TransactionDto;
import com.CardMaster.exceptions.tap.InsufficientLimitException;
import com.CardMaster.exceptions.tap.TransactionNotFoundException;
import com.CardMaster.mapper.tap.TransactionMapper;
import com.CardMaster.model.tap.Transaction;
import com.CardMaster.model.tap.TransactionHold;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionHoldRepository holdRepository;
    private final TransactionMapper transactionMapper;

    /* -------------------------------------------------
       1) AUTHORIZE — create tx(AUTHORIZED) + create hold
       ------------------------------------------------- */
    @Transactional
    public TransactionDto authorize(TransactionDto dto) {
        // Basic input sanity checks
        if (dto == null) throw new IllegalArgumentException("Transaction request cannot be null");
        if (dto.getAccountId() == null) throw new IllegalArgumentException("accountId is required");
        if (dto.getAmount() == null || dto.getAmount() <= 0.0) {
            throw new IllegalArgumentException("amount must be a positive value");
        }
        if (dto.getCurrency() == null || dto.getCurrency().isBlank()) {
            throw new IllegalArgumentException("currency is required");
        }

        // TODO (optional): Check CardAccount.availableLimit here and throw InsufficientLimitException if not enough.
        // Example:
        // double available = tx.getAccountId().getAvailableLimit();
        // if (available < dto.getAmount()) throw new InsufficientLimitException(dto.getAmount());

        // Map DTO → Entity
        Transaction tx = transactionMapper.toEntity(dto);
        tx.setTransactionDate(LocalDateTime.now());
        tx.setStatus(TransactionStatus.AUTHORIZED);

        // Save transaction
        tx = transactionRepository.save(tx);

        // Place a hold for the authorized amount
        TransactionHold hold = new TransactionHold();
        hold.setTransactionId(tx);                 // link hold to this transaction
        hold.setAmount(tx.getAmount());
        hold.setHoldDate(LocalDateTime.now());
        holdRepository.save(hold);

        // TODO (optional): Decrement account available limit here if you maintain it on CardAccount.

        return transactionMapper.toDTO(tx);
    }

    /* -------------------------------------------------
       2) POST — AUTHORIZED -> POSTED, release open hold
       ------------------------------------------------- */
    @Transactional
    public TransactionDto post(Long transactionId) {
        Transaction tx = findOrThrow(transactionId);

        if (tx.getStatus() != TransactionStatus.AUTHORIZED) {
            throw new IllegalStateException("Only AUTHORIZED transactions can be posted");
        }

        // Release the most recent open hold (if any)
        TransactionHold openHold = findLatestOpenHold(transactionId);
        if (openHold != null) {
            openHold.setReleaseDate(LocalDateTime.now());
            holdRepository.save(openHold);
        }

        // Note: available limit was already reserved at authorization.
        tx.setStatus(TransactionStatus.POSTED);
        tx = transactionRepository.save(tx);

        return transactionMapper.toDTO(tx);
    }

    /* -------------------------------------------------
       3) FAIL — release hold (if any), mark FAILED
       ------------------------------------------------- */
    @Transactional
    public TransactionDto fail(Long transactionId, String reason) {
        Transaction tx = findOrThrow(transactionId);

        if (tx.getStatus() == TransactionStatus.POSTED || tx.getStatus() == TransactionStatus.REVERSED) {
            throw new IllegalStateException("Cannot mark POSTED/REVERSED transaction as FAILED");
        }

        // Release open hold and (optionally) restore available limit
        releaseLatestOpenHold(transactionId, true);

        tx.setStatus(TransactionStatus.FAILED);
        tx = transactionRepository.save(tx);
        return transactionMapper.toDTO(tx);
    }

    /* -------------------------------------------------
       4) REVERSE — AUTHORIZED: release hold; POSTED: release/credit back; mark REVERSED
       ------------------------------------------------- */
    @Transactional
    public TransactionDto reverse(Long transactionId, String reason) {
        Transaction tx = findOrThrow(transactionId);

        switch (tx.getStatus()) {
            case AUTHORIZED:
                // Just release the open hold and (optionally) restore available limit
                releaseLatestOpenHold(transactionId, true);
                break;

            case POSTED:
                // Close leftover hold defensively
                releaseLatestOpenHold(transactionId, false);

                // TODO (optional): Credit back available limit on the CardAccount here.
                break;

            default:
                throw new IllegalStateException("Only AUTHORIZED or POSTED transactions can be reversed");
        }

        tx.setStatus(TransactionStatus.REVERSED);
        tx = transactionRepository.save(tx);
        return transactionMapper.toDTO(tx);
    }

    /* -------------------------
       Read helper
       ------------------------- */
    @Transactional(readOnly = true)
    public TransactionDto getById(Long id) {
        return transactionMapper.toDTO(findOrThrow(id));
    }

    /* -------------------------
       Private helpers
       ------------------------- */
    private Transaction findOrThrow(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    /**
     * Returns the latest (by holdDate) open hold for a transaction, or null if none.
     * This is an in-memory filter to avoid adding repository methods right now.
     * You can later replace it with a Spring Data query method:
     *  TransactionHold findTopByTransactionId_TransactionIdAndReleaseDateIsNullOrderByHoldDateDesc(Long txId);
     */
    private TransactionHold findLatestOpenHold(Long transactionId) {
        List<TransactionHold> all = holdRepository.findAll();
        return all.stream()
                .filter(h -> h.getTransactionId() != null
                        && Objects.equals(h.getTransactionId().getTransactionId(), transactionId)
                        && h.getReleaseDate() == null)
                .max(Comparator.comparing(TransactionHold::getHoldDate))
                .orElse(null);
    }

    /**
     * Releases the latest open hold (if any). If restoreLimit=true, add your account credit-back here.
     */
    private void releaseLatestOpenHold(Long transactionId, boolean restoreLimit) {
        TransactionHold openHold = findLatestOpenHold(transactionId);
        if (openHold != null) {
            openHold.setReleaseDate(LocalDateTime.now());
            holdRepository.save(openHold);

            // TODO (optional): if (restoreLimit) { account.setAvailableLimit(account.getAvailableLimit() + openHold.getAmount()); }
        }
    }
}