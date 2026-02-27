package com.CardMaster.service.cias;

import com.CardMaster.Enum.cias.AccountStatus;
import com.CardMaster.Enum.cias.CardStatus;
import com.CardMaster.dao.cias.CardRepository;
import com.CardMaster.dao.cias.CardAccountRepository;
import com.CardMaster.dao.cpl.CardProductRepository;
import com.CardMaster.dao.paa.CustomerRepository;
import com.CardMaster.exceptions.cias.CardIssuanceException;
import com.CardMaster.model.cias.Card;
import com.CardMaster.model.cias.CardAccount;
import com.CardMaster.model.cpl.CardProduct;
import com.CardMaster.model.paa.Customer;
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
    private final CustomerRepository customerRepository;
    private final CardProductRepository productRepository;
    private final JwtUtil jwtUtil;

    // Issue Card → ISSUED card + ACTIVE account
    public CardAccount issueCard(Long customerId, Long productId, Double creditLimit, String token) {
        jwtUtil.extractUsername(token.substring(7));

        if (creditLimit == null || creditLimit <= 0) {
            throw new CardIssuanceException("Credit limit must be positive");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CardIssuanceException("Customer not found with ID: " + customerId));

        CardProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new CardIssuanceException("Product not found with ID: " + productId));

        Card card = new Card();
        card.setCustomer(customer);
        card.setProduct(product);
        card.setMaskedCardNumber("XXXX-XXXX-XXXX-" + (int)(Math.random() * 9000 + 1000));
        card.setExpiryDate(LocalDate.now().plusYears(5));
        card.setCvvHash("dummyHash");
        card.setStatus(CardStatus.ISSUED);

        Card savedCard = cardRepository.save(card);

        CardAccount account = new CardAccount();
        account.setCard(savedCard);
        account.setCreditLimit(creditLimit);
        account.setAvailableLimit(creditLimit);
        account.setOpenDate(LocalDate.now());
        account.setStatus(AccountStatus.ACTIVE);

        CardAccount savedAccount = accountRepository.save(account);

        // link back for easy traversal
        savedCard.setCardAccount(savedAccount);
        cardRepository.save(savedCard);

        return savedAccount;
    }

    // Activate Card → ISSUED → ACTIVE (account stays ACTIVE)
    public Card activateCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardIssuanceException("Card not found with ID: " + cardId));

        if (card.getStatus() != CardStatus.ISSUED) {
            throw new CardIssuanceException("Only ISSUED cards can be activated");
        }

        card.setStatus(CardStatus.ACTIVE);
        return cardRepository.save(card);
    }

    // Block Card (wrong PIN) → BLOCKED card + CLOSED account
    public Card blockCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardIssuanceException("Card not found with ID: " + cardId));

        if (card.getStatus() == CardStatus.BLOCKED) {
            throw new CardIssuanceException("Card is already blocked");
        }

        card.setStatus(CardStatus.BLOCKED);

        // directly traverse relationship instead of repository query
        CardAccount account = card.getCardAccount();
        if (account != null && account.getStatus() == AccountStatus.ACTIVE) {
            account.setStatus(AccountStatus.CLOSED);
            accountRepository.save(account);
        }

        return cardRepository.save(card);
    }

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    public List<CardAccount> getAllAccounts() {
        return accountRepository.findAll();
    }
}

