package com.CardMaster.service.cias;

import com.CardMaster.dao.cias.CardRepository;
import com.CardMaster.dao.cias.CardAccountRepository;
import com.CardMaster.exception.CardIssuanceException;
import com.CardMaster.model.cias.Card;
import com.CardMaster.model.cias.CardAccount;
import com.CardMaster.security.iam.JwtUtil; // assuming you have this utility
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardIssuanceService {

    private final CardRepository cardRepository;
    private final CardAccountRepository accountRepository;
    private final JwtUtil jwtUtil;

    // Issue a new card and account with JWT validation
    public CardAccount issueCard(Long customerId, Long productId, Double creditLimit, String token) {
        // Validate JWT
        jwtUtil.extractUsername(token.substring(7));

        if (creditLimit == null || creditLimit <= 0) {
            throw new CardIssuanceException("Credit limit must be provided and positive");
        }
        if (customerId == null || productId == null) {
            throw new CardIssuanceException("Customer ID and Product ID must be provided");
        }

        try {
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

        } catch (Exception e) {
            throw new CardIssuanceException("Failed to issue card: " + e.getMessage());
        }
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
