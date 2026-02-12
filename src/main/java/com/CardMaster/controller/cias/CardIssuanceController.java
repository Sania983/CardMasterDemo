package com.CardMaster.controller.cias;

import com.CardMaster.model.cias.Card;
import com.CardMaster.model.cias.CardAccount;
import com.CardMaster.service.cias.CardIssuanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardIssuanceController {

    @Autowired
    private CardIssuanceService cardIssuanceService;

    @PostMapping("/issue")
    public CardAccount issueCard(@RequestParam Long customerId,
                                 @RequestParam Long productId,
                                 @RequestParam Double creditLimit) {
        return cardIssuanceService.issueCard(customerId, productId, creditLimit);
    }

    @GetMapping
    public List<Card> getAllCards() {
        return cardIssuanceService.getAllCards();
    }

    @GetMapping("/accounts")
    public List<CardAccount> getAllAccounts() {
        return cardIssuanceService.getAllAccounts();
    }

    @PostMapping("/create")
    public Card createCard(@RequestBody Card card) {
        return cardIssuanceService.saveCard(card);
    }



}
