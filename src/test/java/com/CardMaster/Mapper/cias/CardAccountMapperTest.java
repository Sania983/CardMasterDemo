package com.CardMaster.Mapper.cias;

import com.CardMaster.Enum.cias.AccountStatus;
import com.CardMaster.dao.cias.CardRepository;
import com.CardMaster.dto.cias.CardAccountRequestDto;
import com.CardMaster.dto.cias.CardAccountResponseDto;
import com.CardMaster.mapper.cias.CardAccountMapper;
import com.CardMaster.model.cias.Card;
import com.CardMaster.model.cias.CardAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardAccountMapperTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardAccountMapper mapper;

    @Test
    void toEntity_success() {
        // Arrange
        Long cardId = 1L;
        Card card = Card.builder()
                .cardId(cardId)
                .maskedCardNumber("XXXX-XXXX-XXXX-1234")
                .expiryDate(LocalDate.now().plusYears(5))
                .cvvHash("hash123")
                .status(com.CardMaster.Enum.cias.CardStatus.ISSUED)
                .build();

        CardAccountRequestDto dto = new CardAccountRequestDto();
        dto.setCardId(cardId);
        dto.setCreditLimit(10000.0);
        dto.setAvailableLimit(8000.0);
        dto.setStatus("ACTIVE");

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Act
        CardAccount account = mapper.toEntity(dto);

        // Assert
        assertNotNull(account);
        assertEquals(cardId, account.getCard().getCardId());
        assertEquals(10000.0, account.getCreditLimit());
        assertEquals(8000.0, account.getAvailableLimit());
        assertEquals(AccountStatus.ACTIVE, account.getStatus());

        verify(cardRepository).findById(cardId);
    }

    @Test
    void toEntity_cardNotFound_throwsException() {
        Long cardId = 99L;
        CardAccountRequestDto dto = new CardAccountRequestDto();
        dto.setCardId(cardId);
        dto.setCreditLimit(5000.0);
        dto.setAvailableLimit(5000.0);
        dto.setStatus("ACTIVE");

        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> mapper.toEntity(dto));
    }

    @Test
    void toDTO_success() {
        // Arrange
        Card card = Card.builder()
                .cardId(1L)
                .maskedCardNumber("XXXX-XXXX-XXXX-1234")
                .expiryDate(LocalDate.now().plusYears(5))
                .cvvHash("hash123")
                .status(com.CardMaster.Enum.cias.CardStatus.ACTIVE)
                .build();

        CardAccount account = CardAccount.builder()
                .accountId(10L)
                .card(card)
                .creditLimit(10000.0)
                .availableLimit(9000.0)
                .openDate(LocalDate.now())
                .status(AccountStatus.ACTIVE)
                .build();

        // Act
        CardAccountResponseDto dto = mapper.toDTO(account);

        // Assert
        assertNotNull(dto);
        assertEquals(10L, dto.getAccountId());
        assertEquals(1L, dto.getCardId());
        assertEquals(10000.0, dto.getCreditLimit());
        assertEquals(9000.0, dto.getAvailableLimit());
        assertEquals(AccountStatus.ACTIVE.name(), dto.getStatus());
    }
}
