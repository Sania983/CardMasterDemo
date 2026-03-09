package com.CardMaster.mapper.cias;

import com.CardMaster.Enum.cias.AccountStatus;
import com.CardMaster.dao.cias.CardRepository;
import com.CardMaster.dto.cias.CardAccountRequestDto;
import com.CardMaster.dto.cias.CardAccountResponseDto;
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

class CardAccountMapperTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardAccountMapper accountMapper;

    private CardAccountRequestDto requestDto;
    private Card card;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDto = new CardAccountRequestDto();
        requestDto.setCardId(1L);
        requestDto.setCreditLimit(50000.0);

        card = new Card();
        card.setCardId(1L);
    }

    @Test
    void testToEntity() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        CardAccount account = accountMapper.toEntity(requestDto);

        assertNotNull(account);
        assertEquals(card, account.getCard());
        assertEquals(50000.0, account.getCreditLimit());
        assertEquals(50000.0, account.getAvailableLimit());
        assertEquals(AccountStatus.ACTIVE, account.getStatus());
        assertEquals(LocalDate.now(), account.getOpenDate());
    }

    @Test
    void testToDTO() {
        CardAccount account = new CardAccount();
        account.setAccountId(10L);
        account.setCard(card);
        account.setCreditLimit(50000.0);
        account.setAvailableLimit(45000.0);
        account.setOpenDate(LocalDate.of(2026, 3, 7));
        account.setStatus(AccountStatus.ACTIVE);

        CardAccountResponseDto dto = accountMapper.toDTO(account);

        assertEquals(10L, dto.getAccountId());
        assertEquals(1L, dto.getCardId());
        assertEquals(50000.0, dto.getCreditLimit());
        assertEquals(45000.0, dto.getAvailableLimit());
        assertEquals("ACTIVE", dto.getStatus());
    }
}
