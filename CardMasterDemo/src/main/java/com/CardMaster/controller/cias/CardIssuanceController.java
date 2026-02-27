package com.CardMaster.controller.cias;

import com.CardMaster.model.cias.Card;
import com.CardMaster.model.cias.CardAccount;
import com.CardMaster.service.cias.CardIssuanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardIssuanceController {

    private final CardIssuanceService cardIssuanceService;

    // Issue card + account
    @PostMapping("/issue")
    public ResponseEntity<CardAccount> issueCard(@RequestParam Long customerId,
                                                 @RequestParam Long productId,
                                                 @RequestParam Double creditLimit) {
        return ResponseEntity.ok(
                cardIssuanceService.issueCard(customerId, productId, creditLimit)
        );
    }

    // Activate card
    @PutMapping("/{cardId}/activate")
    public ResponseEntity<Card> activateCard(@PathVariable Long cardId) {
        return ResponseEntity.ok(cardIssuanceService.activateCard(cardId));
    }

    // Get all cards
    @GetMapping
    public ResponseEntity<List<Card>> getAllCards() {
        return ResponseEntity.ok(cardIssuanceService.getAllCards());
    }
}
