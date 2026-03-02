package com.CardMaster.service.cias;

import com.CardMaster.Enum.cias.CardStatus;
import com.CardMaster.dao.cias.CardAccountRepository;
import com.CardMaster.dao.cias.CardRepository;
import com.CardMaster.dto.cias.CardAccountRequestDto;
import com.CardMaster.mapper.cias.CardAccountMapper;
import com.CardMaster.model.cias.Card;
import com.CardMaster.model.cias.CardAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountSetupServiceTest {

    @Mock
    private CardAccountRepository accountRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardAccountMapper accountMapper;

    @InjectMocks
    private AccountSetupService accountSetupService;

    private CardAccountRequestDto requestDto;
    private Card card;
    private CardAccount account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDto = CardAccountRequestDto.builder()
                .cardId(1L)
                .creditLimit(50000.0)
                .availableLimit(50000.0)
                .status("ACTIVE")
                .build();

        card = new Card();
        card.setCardId(1L);
        card.setMaskedCardNumber("**** **** **** 4321");
        card.setExpiryDate(LocalDate.of(2031, 3, 1));
        card.setStatus(CardStatus.ISSUED);

        account = new CardAccount();
        account.setAccountId(10L);
        account.setCard(card);
        account.setCreditLimit(50000.0);
        account.setAvailableLimit(50000.0);
        account.setOpenDate(LocalDate.now());
    }

    @Test
    void testCreateAccount() {
        when(accountMapper.toEntity(requestDto)).thenReturn(account);
        when(cardRepository.save(card)).thenReturn(card);
        when(accountRepository.save(account)).thenReturn(account);

        CardAccount result = accountSetupService.createAccount(requestDto);

        assertNotNull(result);
        assertEquals(CardStatus.ACTIVE, result.getCard().getStatus());
        verify(cardRepository, times(1)).save(card);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testGetAccountById() {
        when(accountRepository.findById(10L)).thenReturn(Optional.of(account));

        CardAccount result = accountSetupService.getAccountById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getAccountId());
        verify(accountRepository, times(1)).findById(10L);
    }

    @Test
    void testGetAccountByIdNotFound() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> accountSetupService.getAccountById(99L));
    }
}
