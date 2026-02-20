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

    // Issue a new card and create its account
    @PostMapping("/issue")
    public ResponseEntity<CardAccount> issueCard(@RequestParam Long customerId,
                                                 @RequestParam Long productId,
                                                 @RequestParam Double creditLimit) {
        return ResponseEntity.ok(
                cardIssuanceService.issueCard(customerId, productId, creditLimit)
        );
    }

    // Get all cards
    @GetMapping
    public ResponseEntity<List<Card>> getAllCards() {
        return ResponseEntity.ok(cardIssuanceService.getAllCards());
    }

    // Get all accounts
    @GetMapping("/accounts")
    public ResponseEntity<List<CardAccount>> getAllAccounts() {
        return ResponseEntity.ok(cardIssuanceService.getAllAccounts());
    }

    // Create a card directly (without issuing account)
    @PostMapping
    public ResponseEntity<Card> createCard(@RequestBody Card card) {
        return ResponseEntity.ok(cardIssuanceService.saveCard(card));
    }
}
