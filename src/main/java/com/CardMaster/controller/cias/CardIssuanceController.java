package com.CardMaster.controller.cias;

import com.CardMaster.dto.cias.CardRequestDto;
import com.CardMaster.dto.cias.CardResponseDto;
import com.CardMaster.mapper.cias.CardMapper;
import com.CardMaster.model.cias.Card;
import com.CardMaster.service.cias.CardIssuanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardIssuanceController {


    private final CardIssuanceService cardService;
    private final CardMapper cardMapper;

    @PostMapping
    public ResponseEntity<CardResponseDto> createCard(@RequestBody CardRequestDto request) {
        Card card = cardService.createCard(request);
        return ResponseEntity.ok(cardMapper.toDTO(card));
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<CardResponseDto> getCard(@PathVariable Long cardId) {
        Card card = cardService.getCardById(cardId);
        return ResponseEntity.ok(cardMapper.toDTO(card));
    }

    @PostMapping("/block/{cardId}")
    public ResponseEntity<CardResponseDto> blockCard(@PathVariable Long cardId) {
        Card card = cardService.blockCard(cardId);
        return ResponseEntity.ok(cardMapper.toDTO(card));
    }
}
