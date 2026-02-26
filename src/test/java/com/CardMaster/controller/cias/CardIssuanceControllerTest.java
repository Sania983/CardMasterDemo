package com.CardMaster.controller.cias;

import com.CardMaster.model.cias.CardAccount;
import com.CardMaster.service.cias.CardIssuanceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardIssuanceControllerTest {

    @Mock
    private CardIssuanceService cardIssuanceService;

    @InjectMocks
    private CardIssuanceController cardIssuanceController;

    @Test
    void testIssueCard_success() {
        CardAccount account = new CardAccount();
        account.setAccountId(1L);
        account.setCreditLimit(50000.0);

        when(cardIssuanceService.issueCard(1L, 2L, 50000.0, "Bearer token"))
                .thenReturn(account);

        CardAccount result = cardIssuanceController.issueCard(1L, 2L, 50000.0, "Bearer token").getBody();

        assertEquals(1L, result.getAccountId());
        assertEquals(50000.0, result.getCreditLimit());
        verify(cardIssuanceService, times(1)).issueCard(1L, 2L, 50000.0, "Bearer token");
    }
}
