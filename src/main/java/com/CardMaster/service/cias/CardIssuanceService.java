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

    // Issue card + create account
    public CardAccount issueCard(Long customerId, Long productId, Double creditLimit) {
        if (creditLimit == null || creditLimit <= 0) {
            throw new CardIssuanceException("Credit limit must be positive");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CardIssuanceException("Customer not found"));

        CardProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new CardIssuanceException("Product not found"));

        // Create Card
        Card card = new Card();
        card.setCustomer(customer);
        card.setProduct(product);
        card.setMaskedCardNumber("XXXX-XXXX-XXXX-" + (int)(Math.random() * 9000 + 1000));
        card.setExpiryDate(LocalDate.now().plusYears(5));
        card.setCvvHash("secureHash");
        card.setStatus(CardStatus.ISSUED);

        Card savedCard = cardRepository.save(card);

        // Create Account linked to card
        CardAccount account = new CardAccount();
        account.setCard(savedCard);
        account.setCreditLimit(creditLimit);
        account.setAvailableLimit(creditLimit);
        account.setOpenDate(LocalDate.now());
        account.setStatus(AccountStatus.ACTIVE);

        return accountRepository.save(account);
    }

    // Activate card
    public Card activateCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardIssuanceException("Card not found"));
        card.setStatus(CardStatus.ACTIVE);
        return cardRepository.save(card);
    }

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }
}
