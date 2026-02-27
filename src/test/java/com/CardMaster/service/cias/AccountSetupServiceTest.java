package com.CardMaster.service.cias;

import com.CardMaster.dao.cias.CardAccountRepository;
import com.CardMaster.exceptions.cias.AccountSetupException;
import com.CardMaster.model.cias.CardAccount;
import com.CardMaster.security.iam.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountSetupServiceTest {

    @Mock
    private CardAccountRepository accountRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AccountSetupService accountSetupService;

    @Test
    void testCreateAccount_success() {
        CardAccount account = new CardAccount();
        account.setCreditLimit(10000.0);

        when(jwtUtil.extractUsername(anyString())).thenReturn("testUser");
        when(accountRepository.save(any(CardAccount.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CardAccount saved = accountSetupService.createAccount(account, "Bearer token");

        assertNotNull(saved);
        assertEquals(10000.0, saved.getCreditLimit());
        assertEquals(saved.getCreditLimit(), saved.getAvailableLimit());
        verify(accountRepository, times(1)).save(any(CardAccount.class));
    }

    @Test
    void testCreateAccount_invalidLimit() {
        CardAccount account = new CardAccount();
        account.setCreditLimit(-100.0);

        when(jwtUtil.extractUsername(anyString())).thenReturn("testUser");

        assertThrows(AccountSetupException.class,
                () -> accountSetupService.createAccount(account, "Bearer token"));
    }
}