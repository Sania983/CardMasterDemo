
package com.CardMaster.mapper.cias;

import com.CardMaster.dto.cias.CardAccountRequestDto;
import com.CardMaster.dto.cias.CardAccountResponseDto;
import com.CardMaster.model.cias.CardAccount;

public class CardAccountMapper {

    public static CardAccount toEntity(CardAccountRequestDto dto) {
        CardAccount account = new CardAccount();
        account.setCardId(dto.getCardId());
        account.setCreditLimit(dto.getCreditLimit());
        account.setAvailableLimit(dto.getAvailableLimit());
        account.setStatus(CardAccount.Status.valueOf(dto.getStatus().toUpperCase()));
        return account;
    }

    public static CardAccountResponseDto toDTO(CardAccount account) {
        CardAccountResponseDto dto = new CardAccountResponseDto();
        dto.setAccountId(account.getAccountId());
        dto.setCardId(account.getCardId());
        dto.setCreditLimit(account.getCreditLimit());
        dto.setAvailableLimit(account.getAvailableLimit());
        dto.setOpenDate(account.getOpenDate());
        dto.setStatus(account.getStatus().name());
        return dto;
    }
}