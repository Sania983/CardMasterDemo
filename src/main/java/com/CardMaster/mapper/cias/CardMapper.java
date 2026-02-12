package com.CardMaster.mapper.cias;

import com.CardMaster.dto.cias.CardRequestDTO;
import com.CardMaster.dto.cias.CardResponseDTO;
import com.CardMaster.model.cias.Card;

public class CardMapper {

    public static Card toEntity(CardRequestDTO dto) {
        Card card = new Card();
        card.setCustomerId(dto.getCustomerId());
        card.setProductId(dto.getProductId());
        card.setMaskedCardNumber(dto.getMaskedCardNumber());
        card.setExpiryDate(dto.getExpiryDate());
        card.setCvvHash(dto.getCvvHash());
        card.setStatus(Card.Status.valueOf(dto.getStatus().toUpperCase()));
        return card;
    }

    public static CardResponseDTO toDTO(Card card) {
        CardResponseDTO dto = new CardResponseDTO();
        dto.setCardId(card.getCardId());
        dto.setCustomerId(card.getCustomerId());
        dto.setProductId(card.getProductId());
        dto.setMaskedCardNumber(card.getMaskedCardNumber());
        dto.setExpiryDate(card.getExpiryDate());
        dto.setStatus(card.getStatus().name());
        return dto;
    }
}