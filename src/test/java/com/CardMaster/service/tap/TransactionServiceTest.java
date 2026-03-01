package com.CardMaster.service.tap;

import com.CardMaster.Enum.tap.TransactionChannel;
import com.CardMaster.dao.tap.TransactionRepository;
import com.CardMaster.dao.tap.TransactionHoldRepository;
import com.CardMaster.Enum.tap.TransactionStatus;
import com.CardMaster.dto.tap.TransactionDto;
import com.CardMaster.exceptions.tap.TransactionNotFoundException;
import com.CardMaster.model.tap.Transaction;
import com.CardMaster.model.tap.TransactionHold;
import com.CardMaster.model.cias.CardAccount;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepo;

    @Mock
    private TransactionHoldRepository holdRepo;

    @InjectMocks
    private TransactionService service;

    private Transaction transaction;
    private CardAccount account;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        account = new CardAccount();
        account.setAccountId(1L);

        transaction = new Transaction();
        transaction.setTransactionId(100L);
        transaction.setAccountId(account);
        transaction.setAmount(500.0);
        transaction.setCurrency("INR");
        transaction.setMerchant("Amazon");
        transaction.setChannel(TransactionChannel.ONLINE);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.AUTHORIZED);
    }

    @Test
    void testAuthorize() {
        when(transactionRepo.save(any(Transaction.class))).thenReturn(transaction);
        when(holdRepo.save(any(TransactionHold.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = service.authorize(transaction);

        assertEquals(TransactionStatus.AUTHORIZED, result.getStatus());
        verify(transactionRepo, times(1)).save(transaction);
        verify(holdRepo, times(1)).save(any(TransactionHold.class));
    }

    @Test
    void testPostSuccess() {
        when(transactionRepo.findById(100L)).thenReturn(Optional.of(transaction));
        when(transactionRepo.save(any(Transaction.class))).thenReturn(transaction);
        TransactionHold hold = new TransactionHold();
        hold.setTransactionId(transaction);
        hold.setHoldId(200L);
        hold.setAmount(500.0);
        hold.setHoldDate(LocalDateTime.now());
        when(holdRepo.findAll()).thenReturn(Collections.singletonList(hold));

        TransactionDto dto = service.post(100L);

        assertEquals(TransactionStatus.POSTED, dto.getStatus());
        assertEquals(100L, dto.getTransactionId());
        verify(transactionRepo, times(1)).save(transaction);
        verify(holdRepo, times(1)).save(hold);
    }

    @Test
    void testPostThrowsWhenNotAuthorized() {
        transaction.setStatus(TransactionStatus.POSTED);
        when(transactionRepo.findById(100L)).thenReturn(Optional.of(transaction));

        assertThrows(IllegalStateException.class, () -> service.post(100L));
    }

    @Test
    void testReverse() {
        when(transactionRepo.findById(100L)).thenReturn(Optional.of(transaction));
        when(transactionRepo.save(any(Transaction.class))).thenReturn(transaction);
        TransactionHold hold = new TransactionHold();
        hold.setTransactionId(transaction);
        hold.setHoldId(200L);
        hold.setAmount(500.0);
        hold.setHoldDate(LocalDateTime.now());
        when(holdRepo.findAll()).thenReturn(Collections.singletonList(hold));

        Transaction result = service.reverse(100L);

        assertEquals(TransactionStatus.REVERSED, result.getStatus());
        verify(transactionRepo, times(1)).save(transaction);
        verify(holdRepo, times(1)).save(hold);
    }

    @Test
    void testGetByIdNotFound() {
        when(transactionRepo.findById(999L)).thenReturn(Optional.empty());
        assertThrows(TransactionNotFoundException.class, () -> service.getById(999L));
    }

    @Test
    void testListAll() {
        when(transactionRepo.findAll()).thenReturn(Collections.singletonList(transaction));
        assertEquals(1, service.listAll().size());
    }
}
