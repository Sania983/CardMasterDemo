package com.CardMaster.controller.cias;

import com.CardMaster.model.cias.CardAccount;
import com.CardMaster.service.cias.AccountSetupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountSetupControllerTest {

    @Mock
    private AccountSetupService accountSetupService;

    @InjectMocks
    private AccountSetupController accountSetupController;

    @Test
    void testCreateAccount_success() {
        CardAccount account = new CardAccount();
        account.setAccountId(1L);
        account.setCreditLimit(10000.0);

        when(accountSetupService.createAccount(any(CardAccount.class), eq("Bearer token")))
                .thenReturn(account);

        ResponseEntity<CardAccount> response =
                accountSetupController.createAccount(account, "Bearer token");

        // ✅ Correct assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getAccountId());
        assertEquals(10000.0, response.getBody().getCreditLimit());

        verify(accountSetupService, times(1))
                .createAccount(any(CardAccount.class), eq("Bearer token"));
    }
}
