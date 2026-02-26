//package com.CardMaster.service.tap;
//
//import com.CardMaster.dao.tap.CardAccountRepository;
//import com.CardMaster.dao.tap.TransactionHoldRepository;
//import com.CardMaster.dao.tap.TransactionRepository;
//import com.CardMaster.exceptions.tap.ResourceNotFoundException; // keep if you already use this elsewhere
//import com.CardMaster.exceptions.tap.InsufficientLimitException;
//import com.CardMaster.exceptions.tap.TransactionNotFoundException;
//import com.CardMaster.model.accounts.CardAccount;
//import com.CardMaster.model.transactions.Transaction;
//import com.CardMaster.model.transactions.TransactionHold;
//import com.CardMaster.model.transactions.TransactionStatus;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
///**
// * Beginner-friendly TransactionService aligned to Module 4.6:
// *  - authorize(): create AUTHORIZED tx, place TransactionHold, reduce availableLimit
// *  - post(): AUTHORIZED -> POSTED, close hold (limit already reserved)
// *  - fail(): release hold + restore limit -> FAILED
// *  - reverse(): if AUTHORIZED -> release hold; if POSTED -> credit back -> REVERSED
// *
// * Uses your tap exceptions:
// *  - InsufficientLimitException for limit shortfalls
// *  - TransactionNotFoundException when tx id is missing/invalid
// *
// * Fields/flow come from CardMaster Module 4.6 design (Transaction & TransactionHold, statuses). [1](https://cognizantonline-my.sharepoint.com/personal/2465986_cognizant_com/Documents/Microsoft%20Copilot%20Chat%20Files/CardMaster%20-%20Credit%20Card%20Issuance,%20Transactions%20%26%20Risk%20Management%20System.pdf)
// */
//@Service
//@RequiredArgsConstructor
//public class TransactionService {
//
//    private final TransactionRepository transactionRepo;
//    private final TransactionHoldRepository holdRepo;
//    private final CardAccountRepository accountRepo;
//
//    /* -------------------------------------------------
//       1) AUTHORIZE — create tx, place hold, reduce limit
//       ------------------------------------------------- */
//    @Transactional
//    public Transaction authorize(Transaction request) {
//        // --- basic request validation (clear messages for beginners) ---
//        if (request == null) {
//            throw new IllegalArgumentException("Transaction request cannot be null");
//        }
//        if (request.getAccountId() == null) {
//            throw new IllegalArgumentException("accountId is required");
//        }
//        if (request.getAmount() == null || request.getAmount().signum() <= 0) {
//            throw new IllegalArgumentException("amount must be a positive value");
//        }
//        if (isBlank(request.getCurrency())) {
//            throw new IllegalArgumentException("currency is required");
//        }
//
//        // --- load account and check status & available limit (Module 4.5/4.6) ---
//        CardAccount account = accountRepo.findById(request.getAccountId())
//                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + request.getAccountId()));
//
//        ensureAccountActive(account);
//
//        // check limit before authorizing
//        if (isInsufficient(account.getAvailableLimit(), request.getAmount())) {
//            throw new InsufficientLimitException("Insufficient available limit");
//        }
//
//        // --- build and save AUTHORIZED transaction ---
//        Transaction tx = new Transaction();
//        tx.setAccountId(request.getAccountId());
//        tx.setAmount(request.getAmount());
//        tx.setCurrency(request.getCurrency());
//        tx.setMerchant(request.getMerchant());
//        tx.setChannel(request.getChannel());       // POS / Online / ATM per Module 4.6
//        tx.setTransactionDate(LocalDateTime.now());
//        tx.setStatus(TransactionStatus.AUTHORIZED);
//        tx = transactionRepo.save(tx);
//
//        // --- create a hold (Module 4.6 TransactionHold) and reduce availableLimit ---
//        TransactionHold hold = new TransactionHold();
//        hold.setTransactionId(tx.getTransactionId());
//        hold.setAmount(tx.getAmount());
//        hold.setHoldDate(LocalDateTime.now());
//        holdRepo.save(hold);
//
//        account.setAvailableLimit(account.getAvailableLimit().subtract(tx.getAmount()));
//        accountRepo.save(account);
//
//        return tx;
//    }
//
//    /* -------------------------------------------------
//       2) POST — AUTHORIZED -> POSTED, close hold
//       ------------------------------------------------- */
//    @Transactional
//    public Transaction post(Long transactionId) {
//        Transaction tx = transactionRepo.findById(transactionId)
//                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found: " + transactionId));
//
//        if (tx.getStatus() != TransactionStatus.AUTHORIZED) {
//            // Using IllegalStateException here keeps it simple.
//            // If you prefer a tap exception, create one like InvalidTransactionStateException.
//            throw new IllegalStateException("Only AUTHORIZED transactions can be posted");
//        }
//
//        // close the hold (availableLimit was already reduced during authorization)
//        TransactionHold hold = holdRepo.findByTransactionId(tx.getTransactionId()).orElse(null);
//        if (hold != null && hold.getReleaseDate() == null) {
//            hold.setReleaseDate(LocalDateTime.now());
//            holdRepo.save(hold);
//        }
//
//        tx.setStatus(TransactionStatus.POSTED);
//        return transactionRepo.save(tx);
//    }
//
//    /* -------------------------------------------------
//       3) FAIL — release hold, restore limit, set FAILED
//       ------------------------------------------------- */
//    @Transactional
//    public Transaction fail(Long transactionId, String reason) {
//        Transaction tx = transactionRepo.findById(transactionId)
//                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found: " + transactionId));
//
//        // Safety: don't allow changing a finalized/reversed tx
//        if (tx.getStatus() == TransactionStatus.POSTED || tx.getStatus() == TransactionStatus.REVERSED) {
//            throw new IllegalStateException("Cannot mark POSTED/REVERSED transaction as FAILED");
//        }
//
//        releaseHoldAndRestoreLimit(tx);
//
//        tx.setStatus(TransactionStatus.FAILED);
//        return transactionRepo.save(tx);
//    }
//
//    /* -------------------------------------------------
//       4) REVERSE — AUTHORIZED: release hold; POSTED: credit back; set REVERSED
//       ------------------------------------------------- */
//    @Transactional
//    public Transaction reverse(Long transactionId, String reason) {
//        Transaction tx = transactionRepo.findById(transactionId)
//                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found: " + transactionId));
//
//        switch (tx.getStatus()) {
//            case AUTHORIZED:
//                // Just release hold and restore limit
//                releaseHoldAndRestoreLimit(tx);
//                break;
//            case POSTED:
//                // Credit back available limit (simple approach for beginners)
//                CardAccount acc = accountRepo.findById(tx.getAccountId())
//                        .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + tx.getAccountId()));
//                acc.setAvailableLimit(acc.getAvailableLimit().add(tx.getAmount()));
//                accountRepo.save(acc);
//                break;
//            default:
//                throw new IllegalStateException("Only AUTHORIZED or POSTED transactions can be reversed");
//        }
//
//        tx.setStatus(TransactionStatus.REVERSED);
//        return transactionRepo.save(tx);
//    }
//
//    /* -------------------------
//       Helper methods
//       ------------------------- */
//    private void releaseHoldAndRestoreLimit(Transaction tx) {
//        TransactionHold hold = holdRepo.findByTransactionId(tx.getTransactionId()).orElse(null);
//        if (hold != null && hold.getReleaseDate() == null) {
//            hold.setReleaseDate(LocalDateTime.now());
//            holdRepo.save(hold);
//
//            CardAccount acc = accountRepo.findById(tx.getAccountId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + tx.getAccountId()));
//            acc.setAvailableLimit(acc.getAvailableLimit().add(hold.getAmount()));
//            accountRepo.save(acc);
//        }
//    }
//
//    private void ensureAccountActive(CardAccount account) {
//        if (account.getStatus() == null || !"Active".equalsIgnoreCase(account.getStatus())) {
//            throw new IllegalStateException("Account is not Active");
//        }
//    }
//
//    private boolean isInsufficient(BigDecimal available, BigDecimal amount) {
//        return available == null || available.compareTo(amount) < 0;
//    }
//
//    private boolean isBlank(String s) {
//        return s == null || s.trim().isEmpty();
//    }
//}