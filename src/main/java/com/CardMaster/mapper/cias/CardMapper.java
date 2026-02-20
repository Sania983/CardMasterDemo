package com.CardMaster.mapper.cias;

import com.CardMaster.dto.cias.CardRequestDto;
import com.CardMaster.dto.cias.CardResponseDto;
import com.CardMaster.model.cias.Card;

public class CardMapper {

    public static Card toEntity(CardRequestDto dto) {
        Card card = new Card();
        card.setCustomerId(dto.getCustomerId());
        card.setProductId(dto.getProductId());
        card.setMaskedCardNumber(dto.getMaskedCardNumber());
        card.setExpiryDate(dto.getExpiryDate());
        card.setCvvHash(dto.getCvvHash());
        card.setStatus(Card.Status.valueOf(dto.getStatus().toUpperCase()));
        return card;
    }

    public static CardResponseDto toDTO(Card card) {
        CardResponseDto dto = new CardResponseDto();
        dto.setCardId(card.getCardId());
        dto.setCustomerId(card.getCustomerId());
        dto.setProductId(card.getProductId());
        dto.setMaskedCardNumber(card.getMaskedCardNumber());
        dto.setExpiryDate(card.getExpiryDate());
        dto.setStatus(card.getStatus().name());
        return dto;
    }
}