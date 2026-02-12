
package com.CardMaster.mapper;

import com.CardMaster.dto.CardAccountRequestDTO;
import com.CardMaster.dto.CardAccountResponseDTO;
import com.CardMaster.dto.CardAccountResponseDTO;
import com.CardMaster.model.CardAccount;

public class CardAccountMapper {

    public static CardAccount toEntity(CardAccountRequestDTO dto) {
        CardAccount account = new CardAccount();
        account.setCardId(dto.getCardId());
        account.setCreditLimit(dto.getCreditLimit());
        account.setAvailableLimit(dto.getAvailableLimit());
        account.setStatus(CardAccount.Status.valueOf(dto.getStatus().toUpperCase()));
        return account;
    }

    public static CardAccountResponseDTO toDTO(CardAccount account) {
        CardAccountResponseDTO dto = new CardAccountResponseDTO();
        dto.setAccountId(account.getAccountId());
        dto.setCardId(account.getCardId());
        dto.setCreditLimit(account.getCreditLimit());
        dto.setAvailableLimit(account.getAvailableLimit());
        dto.setOpenDate(account.getOpenDate());
        dto.setStatus(account.getStatus().name());
        return dto;
    }
}