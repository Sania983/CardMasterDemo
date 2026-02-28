package com.CardMaster.service.cias;

import com.CardMaster.dao.cias.CardAccountRepository;
import com.CardMaster.exceptions.cias.AccountSetupException;
import com.CardMaster.model.cias.CardAccount;
import com.CardMaster.security.iam.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountSetupServiceTest {

    @Mock private CardAccountRepository accountRepository;
    @Mock private JwtUtil jwtUtil;

    @InjectMocks private AccountSetupService accountSetupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAccount_success() {
        CardAccount account = new CardAccount();
        account.setCreditLimit(5000.0);

        when(jwtUtil.extractUsername(anyString())).thenReturn("testUser");
        when(accountRepository.save(any(CardAccount.class))).thenAnswer(invocation -> {
            CardAccount acc = invocation.getArgument(0);
            acc.setAccountId(1L);
            return acc;
        });

        CardAccount result = accountSetupService.createAccount(account, "Bearer token");

        assertNotNull(result);
        assertEquals(1L, result.getAccountId());
        assertEquals(5000.0, result.getCreditLimit());
        assertEquals(5000.0, result.getAvailableLimit());
        assertEquals("ACTIVE", result.getStatus().name());
    }

    @Test
    void createAccount_invalidCreditLimit_throwsException() {
        CardAccount account = new CardAccount();
        account.setCreditLimit(-100.0);

        when(jwtUtil.extractUsername(anyString())).thenReturn("testUser");

        assertThrows(AccountSetupException.class,
                () -> accountSetupService.createAccount(account, "Bearer token"));
        verify(accountRepository, never()).save(any(CardAccount.class));
    }

    @Test
    void getAllAccounts_success() {
        when(accountRepository.findAll()).thenReturn(List.of(new CardAccount()));
        List<CardAccount> accounts = accountSetupService.getAllAccounts();
        assertFalse(accounts.isEmpty());
    }

    @Test
    void getAccountById_success() {
        CardAccount account = new CardAccount();
        account.setAccountId(1L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        CardAccount result = accountSetupService.getAccountById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getAccountId());
    }

    @Test
    void getAccountById_notFound_returnsNull() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());
        CardAccount result = accountSetupService.getAccountById(99L);
        assertNull(result);
    }
}
