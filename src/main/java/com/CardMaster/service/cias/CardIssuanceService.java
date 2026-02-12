package com.CardMaster.service.cias;

import com.CardMaster.dao.cias.CardRepository;
import com.CardMaster.dao.cias.CardAccountRepository;
import com.CardMaster.model.cias.Card;
import com.CardMaster.model.cias.CardAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CardIssuanceService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardAccountRepository accountRepository;

    // Issue a new card and account
    public CardAccount issueCard(Long customerId, Long productId, Double creditLimit) {
        Card card = new Card();
        card.setCustomerId(customerId);
        card.setProductId(productId);
        card.setMaskedCardNumber("XXXX-XXXX-XXXX-" + (int)(Math.random() * 9000 + 1000));
        card.setExpiryDate(LocalDate.now().plusYears(5));
        card.setCvvHash("dummyHash");
        card.setStatus(Card.Status.Issued);

        Card savedCard = cardRepository.save(card);

        CardAccount account = new CardAccount();
        account.setCardId(savedCard.getCardId());
        account.setCreditLimit(creditLimit);
        account.setAvailableLimit(creditLimit);
        account.setOpenDate(LocalDate.now());
        account.setStatus(CardAccount.Status.Active);

        return accountRepository.save(account);
    }

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    public List<CardAccount> getAllAccounts() {
        return accountRepository.findAll();
    }


    public Card saveCard(Card card) {
        return cardRepository.save(card);
    }
}
