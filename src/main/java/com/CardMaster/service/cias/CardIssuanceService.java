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
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new CardIssuanceException("Customer not found with ID: " + customerId));

            CardProduct product = productRepository.findById(productId)
                    .orElseThrow(() -> new CardIssuanceException("Product not found with ID: " + productId));

            Card card = new Card();
            card.setCustomer(customer);   // set entity, JPA stores FK
            card.setProduct(product);     // set entity, JPA stores FK
            card.setMaskedCardNumber("XXXX-XXXX-XXXX-" + (int)(Math.random() * 9000 + 1000));
            card.setExpiryDate(LocalDate.now().plusYears(5));
            card.setCvvHash("dummyHash");
            card.setStatus(CardStatus.ISSUED);

            Card savedCard = cardRepository.save(card);

            CardAccount account = new CardAccount();
            account.setCard(savedCard);   // reference Card entity
            account.setCreditLimit(creditLimit);
            account.setAvailableLimit(creditLimit);
            account.setOpenDate(LocalDate.now());
            account.setStatus(AccountStatus.ACTIVE);

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