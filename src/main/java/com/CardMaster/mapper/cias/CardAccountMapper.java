package com.CardMaster.mapper.cias;

import com.CardMaster.model.cias.CardAccount;
import com.CardMaster.Enum.cias.AccountStatus;
import com.CardMaster.dto.cias.CardAccountRequestDto;
import com.CardMaster.dto.cias.CardAccountResponseDto;
import com.CardMaster.model.cias.Card;
import com.CardMaster.dao.cias.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardAccountMapper {

    private final CardRepository cardRepository;

    public CardAccount toEntity(CardAccountRequestDto dto) {
        Card card = cardRepository.findById(dto.getCardId())
                .orElseThrow(() -> new IllegalArgumentException("Card not found with ID: " + dto.getCardId()));

        CardAccount account = new CardAccount();
        account.setCard(card);
        account.setCreditLimit(dto.getCreditLimit());
        account.setAvailableLimit(dto.getAvailableLimit());
        account.setStatus(AccountStatus.valueOf(dto.getStatus().toUpperCase()));
        return account;
    }

    public CardAccountResponseDto toDTO(CardAccount account) {
        CardAccountResponseDto dto = new CardAccountResponseDto();
        dto.setAccountId(account.getAccountId());
        dto.setCardId(account.getCard().getCardId());
        dto.setCreditLimit(account.getCreditLimit());
        dto.setAvailableLimit(account.getAvailableLimit());
        dto.setOpenDate(account.getOpenDate());
        dto.setStatus(account.getStatus().name());
        return dto;
    }
}
