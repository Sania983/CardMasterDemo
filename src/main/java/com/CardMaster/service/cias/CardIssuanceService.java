package com.CardMaster.service.cias;

import com.CardMaster.Enum.cias.AccountStatus;
import com.CardMaster.Enum.cias.CardStatus;
import com.CardMaster.dao.cias.CardRepository;
import com.CardMaster.dao.cias.CardAccountRepository;
import com.CardMaster.model.cias.Card;
import com.CardMaster.model.cias.CardAccount;
import com.CardMaster.model.paa.Customer;
import com.CardMaster.model.cpl.CardProduct;
import com.CardMaster.dao.paa.CustomerRepository;
import com.CardMaster.dao.cpl.CardProductRepository;
import com.CardMaster.exceptions.cias.CardIssuanceException;
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
<<<<<<< HEAD
    private final JwtUtil jwtUtil;

    // Issue Card → ISSUED card + ACTIVE account
    public CardAccount issueCard(Long customerId, Long productId, Double creditLimit, String token) {
        jwtUtil.extractUsername(token.substring(7));
=======
>>>>>>> 782e4170f7daa7d67540b3b67ab0dc8154d89793

    // Issue card + create account
    public CardAccount issueCard(Long customerId, Long productId, Double creditLimit) {
        if (creditLimit == null || creditLimit <= 0) {
            throw new CardIssuanceException("Credit limit must be positive");
        }

        Customer customer = customerRepository.findById(customerId)
<<<<<<< HEAD
                .orElseThrow(() -> new CardIssuanceException("Customer not found with ID: " + customerId));

        CardProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new CardIssuanceException("Product not found with ID: " + productId));

=======
                .orElseThrow(() -> new CardIssuanceException("Customer not found"));

        CardProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new CardIssuanceException("Product not found"));

        // Create Card
>>>>>>> 782e4170f7daa7d67540b3b67ab0dc8154d89793
        Card card = new Card();
        card.setCustomer(customer);
        card.setProduct(product);
        card.setMaskedCardNumber("XXXX-XXXX-XXXX-" + (int)(Math.random() * 9000 + 1000));
        card.setExpiryDate(LocalDate.now().plusYears(5));
<<<<<<< HEAD
        card.setCvvHash("dummyHash");
=======
        card.setCvvHash("secureHash");
>>>>>>> 782e4170f7daa7d67540b3b67ab0dc8154d89793
        card.setStatus(CardStatus.ISSUED);

        Card savedCard = cardRepository.save(card);

<<<<<<< HEAD
=======
        // Create Account linked to card
>>>>>>> 782e4170f7daa7d67540b3b67ab0dc8154d89793
        CardAccount account = new CardAccount();
        account.setCard(savedCard);
        account.setCreditLimit(creditLimit);
        account.setAvailableLimit(creditLimit);
        account.setOpenDate(LocalDate.now());
        account.setStatus(AccountStatus.ACTIVE);

<<<<<<< HEAD
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
=======
        return accountRepository.save(account);
    }

    // Activate card
    public Card activateCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardIssuanceException("Card not found"));
        card.setStatus(CardStatus.ACTIVE);
        return cardRepository.save(card);
>>>>>>> 782e4170f7daa7d67540b3b67ab0dc8154d89793
    }

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }
<<<<<<< HEAD

    public List<CardAccount> getAllAccounts() {
        return accountRepository.findAll();
    }
=======
>>>>>>> 782e4170f7daa7d67540b3b67ab0dc8154d89793
}
