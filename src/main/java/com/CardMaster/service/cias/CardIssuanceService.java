package com.CardMaster.service.cias;

import com.CardMaster.Enum.cias.CardStatus;
import com.CardMaster.dao.cias.CardRepository;
import com.CardMaster.dto.cias.CardRequestDto;
import com.CardMaster.mapper.cias.CardMapper;
import com.CardMaster.model.cias.Card;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardIssuanceService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    public Card createCard(CardRequestDto requestDto) {
        Card card = cardMapper.toEntity(requestDto);
        card.setStatus(CardStatus.ISSUED); // always ISSUED on creation
        return cardRepository.save(card);
    }

    public Card getCardById(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found with ID: " + cardId));
    }

    public Card blockCard(Long cardId) {
        Card card = getCardById(cardId);
        card.setStatus(CardStatus.BLOCKED);
        return cardRepository.save(card);
    }
}
