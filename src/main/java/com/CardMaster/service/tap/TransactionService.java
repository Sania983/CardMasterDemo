package com.CardMaster.service.tap;

import com.CardMaster.dao.accounts.CardAccountRepository;
import com.CardMaster.dao.transactions.TransactionHoldRepository;
import com.CardMaster.dao.transactions.TransactionRepository;
import com.CardMaster.exceptions.ResourceNotFoundException;
import com.CardMaster.model.accounts.CardAccount;
import com.CardMaster.model.transactions.Transaction;
import com.CardMaster.model.transactions.TransactionHold;
import com.CardMaster.model.transactions.TransactionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Beginner-friendly TransactionService:
 * - authorize(): create tx, set AUTHORIZED, place hold, reduce available limit
 * - post(): AUTHORIZED -> POSTED (close hold; limit already reserved)
 * - fail(): release hold, restore limit, mark FAILED
 * - reverse(): if AUTHORIZED -> release hold; if POSTED -> credit back limit; mark REVERSED
 *
 * Matches Module 4.6 design (Transaction & TransactionHold, statuses), and uses Account.availableLimit
 * from Module 4.5 CardAccount to keep it realistic but simple.  [1](https://cognizantonline-my.sharepoint.com/personal/2465986_cognizant_com/Documents/Microsoft%20Copilot%20Chat%20Files/CardMaster%20-%20Credit%20Card%20Issuance,%20Transactions%20%26%20Risk%20Management%20System.pdf)
 */
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepo;
    private final TransactionHoldRepository holdRepo;
    private final CardAccountRepository accountRepo;

    /* -------------------------
       1) AUTHORIZE (Place Hold)
       ------------------------- */
    @Transactional
    public Transaction authorize(Transaction tx) {
        // Basic validations for beginners
        if (tx == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }
        if (tx.getAccountId() == null) {
            throw new IllegalArgumentException("accountId is required");
        }
        if (tx.getAmount() == null || tx.getAmount().signum() <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        if (tx.getCurrency() == null || tx.getCurrency().isBlank()) {
            throw new IllegalArgumentException("currency is required");
        }

        // Load account and check status/limit
        CardAccount account = accountRepo.findById(tx.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + tx.getAccountId()));

        if (!"Active".equalsIgnoreCase(account.getStatus())) {
            throw new IllegalStateException("Account is not Active");
        }
        if (account.getAvailableLimit() == null || account.getAvailableLimit().compareTo(tx.getAmount()) < 0) {
            throw new IllegalStateException("Insufficient available limit");
        }

        // Set tx status & date, then save
        tx.setStatus(TransactionStatus.AUTHORIZED);
        tx.setTransactionDate(LocalDateTime.now());
        Transaction saved = transactionRepo.save(tx);

        // Create a hold record (Module 4.6 defines TransactionHold)  [1](https://cognizantonline-my.sharepoint.com/personal/2465986_cognizant_com/Documents/Microsoft%20Copilot%20Chat%20Files/CardMaster%20-%20Credit%20Card%20Issuance,%20Transactions%20%26%20Risk%20Management%20System.pdf)
        TransactionHold hold = new TransactionHold();
        hold.setTransactionId(saved.getTransactionId());
        hold.setAmount(saved.getAmount());
        hold.setHoldDate(LocalDateTime.now());
        holdRepo.save(hold);

        // Reduce available limit immediately
        account.setAvailableLimit(account.getAvailableLimit().subtract(saved.getAmount()));
        accountRepo.save(account);

        return saved;
    }

    /* -------------------------
       2) POST (Capture / Settlement)
       ------------------------- */
    @Transactional
    public Transaction post(Long id) {
        Transaction tx = transactionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + id));

        if (tx.getStatus() != TransactionStatus.AUTHORIZED) {
            throw new IllegalStateException("Only AUTHORIZED transactions can be posted");
        }

        // Close (release) the hold. We do NOT change available limit here,
        // because it was already reduced at authorization time.
        TransactionHold hold = holdRepo.findByTransactionId(tx.getTransactionId()).orElse(null);
        if (hold != null && hold.getReleaseDate() == null) {
            hold.setReleaseDate(LocalDateTime.now());
            holdRepo.save(hold);
        }

        tx.setStatus(TransactionStatus.POSTED);
        return transactionRepo.save(tx);
    }

    /* -------------------------
       3) FAIL (Authorization failed later)
       ------------------------- */
    @Transactional
    public Transaction fail(Long id, String reason) {
        Transaction tx = transactionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + id));

        if (tx.getStatus() == TransactionStatus.POSTED || tx.getStatus() == TransactionStatus.REVERSED) {
            throw new IllegalStateException("Cannot mark POSTED/REVERSED transaction as FAILED");
        }

        // Release hold and restore limit
        releaseHoldAndRestoreLimit(tx);

        tx.setStatus(TransactionStatus.FAILED);
        return transactionRepo.save(tx);
    }

    /* -------------------------
       4) REVERSE (Void/Refund)
       ------------------------- */
    @Transactional
    public Transaction reverse(Long id, String reason) {
        Transaction tx = transactionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + id));

        switch (tx.getStatus()) {
            case AUTHORIZED:
                // Just release the hold and give back limit
                releaseHoldAndRestoreLimit(tx);
                break;

            case POSTED:
                // For simplicity: credit back available limit now
                CardAccount acc = accountRepo.findById(tx.getAccountId())
                        .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + tx.getAccountId()));
                acc.setAvailableLimit(acc.getAvailableLimit().add(tx.getAmount()));
                accountRepo.save(acc);
                break;

            default:
                throw new IllegalStateException("Only AUTHORIZED or POSTED transactions can be reversed");
        }

        tx.setStatus(TransactionStatus.REVERSED);
        return transactionRepo.save(tx);
    }

    /* -------------------------
       Helpers (kept very simple)
       ------------------------- */
    private void releaseHoldAndRestoreLimit(Transaction tx) {
        TransactionHold hold = holdRepo.findByTransactionId(tx.getTransactionId()).orElse(null);
        if (hold != null && hold.getReleaseDate() == null) {
            hold.setReleaseDate(LocalDateTime.now());
            holdRepo.save(hold);

            CardAccount acc = accountRepo.findById(tx.getAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + tx.getAccountId()));
            acc.setAvailableLimit(acc.getAvailableLimit().add(hold.getAmount()));
            accountRepo.save(acc);
        }
    }
}
