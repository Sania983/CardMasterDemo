package com.CardMaster.service.cias;

import com.CardMaster.Enum.cias.CardStatus;
import com.CardMaster.dao.cias.CardRepository;
import com.CardMaster.dto.cias.CardRequestDto;
import com.CardMaster.mapper.cias.CardMapper;
import com.CardMaster.model.cias.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardIssuanceServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private CardIssuanceService cardIssuanceService;

    private CardRequestDto requestDto;
    private Card card;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDto = CardRequestDto.builder()
                .customerId(123L)
                .productId(456L)
                .maskedCardNumber("**** **** **** 4321")
                .expiryDate(LocalDate.of(2031, 3, 1))
                .cvvHash("dummyHash")
                .status("ISSUED")
                .build();

        card = new Card();
        card.setCardId(1L);
        card.setMaskedCardNumber("**** **** **** 4321");
        card.setExpiryDate(LocalDate.of(2031, 3, 1));
        card.setCvvHash("dummyHash");
        card.setStatus(CardStatus.ISSUED);
    }

    @Test
    void testCreateCard() {
        when(cardMapper.toEntity(requestDto)).thenReturn(card);
        when(cardRepository.save(card)).thenReturn(card);

        Card result = cardIssuanceService.createCard(requestDto);

        assertNotNull(result);
        assertEquals(CardStatus.ISSUED, result.getStatus());
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    void testGetCardById() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        Card result = cardIssuanceService.getCardById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getCardId());
        verify(cardRepository, times(1)).findById(1L);
    }

    @Test
    void testActivateCard() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);

        Card result = cardIssuanceService.activateCard(1L);

        assertEquals(CardStatus.ACTIVE, result.getStatus());
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    void testActivateCardInvalidState() {
        card.setStatus(CardStatus.BLOCKED);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        assertThrows(IllegalStateException.class, () -> cardIssuanceService.activateCard(1L));
    }

    @Test
    void testBlockCard() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);

        Card result = cardIssuanceService.blockCard(1L);

        assertEquals(CardStatus.BLOCKED, result.getStatus());
        verify(cardRepository, times(1)).save(card);
    }
}
