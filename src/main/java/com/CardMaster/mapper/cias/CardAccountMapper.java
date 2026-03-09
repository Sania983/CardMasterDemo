package com.CardMaster.mapper.cias;

import com.CardMaster.dto.cias.CardAccountRequestDto;
import com.CardMaster.dto.cias.CardAccountResponseDto;
import com.CardMaster.model.cias.Card;
import com.CardMaster.model.cias.CardAccount;
import com.CardMaster.Enum.cias.AccountStatus;
import com.CardMaster.dao.cias.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CardAccountMapper {

    private final CardRepository cardRepository;

    // Request DTO -> Entity
    public CardAccount toEntity(CardAccountRequestDto dto) {
        Card card = cardRepository.findById(dto.getCardId())
                .orElseThrow(() -> new IllegalArgumentException("Card not found with ID: " + dto.getCardId()));

        CardAccount account = new CardAccount();
        account.setCard(card);
        account.setCreditLimit(dto.getCreditLimit());
        account.setAvailableLimit(dto.getCreditLimit()); // available = credit limit initially
        account.setOpenDate(LocalDate.now());            // auto-set today's date
        account.setStatus(AccountStatus.ACTIVE);         // enforce ACTIVE at creation
        return account;
    }

    // Entity -> Response DTO
    public CardAccountResponseDto toDTO(CardAccount account) {
        CardAccountResponseDto dto = new CardAccountResponseDto();
        dto.setAccountId(account.getAccountId());
        dto.setCardId(account.getCard().getCardId()); // only reference cardId
        dto.setCreditLimit(account.getCreditLimit());
        dto.setAvailableLimit(account.getAvailableLimit());
        dto.setOpenDate(account.getOpenDate());
        dto.setStatus(account.getStatus().name());
        return dto;
    }
}
