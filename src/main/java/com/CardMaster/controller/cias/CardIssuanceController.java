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
    //later added
    @PostMapping("/{id}/activate")
    public ResponseEntity<Card> activateCard(@PathVariable Long id) {
        return ResponseEntity.ok(cardIssuanceService.activateCard(id));
    }

    @PostMapping("/{id}/block")
    public ResponseEntity<Card> blockCard(@PathVariable Long id) {
        return ResponseEntity.ok(cardIssuanceService.blockCard(id));
    }

    //till here

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
<<<<<<< HEAD

    // Get all accounts
    @GetMapping("/accounts")
    public ResponseEntity<List<CardAccount>> getAllAccounts() {
        return ResponseEntity.ok(cardIssuanceService.getAllAccounts());
    }

=======
>>>>>>> 782e4170f7daa7d67540b3b67ab0dc8154d89793
}
