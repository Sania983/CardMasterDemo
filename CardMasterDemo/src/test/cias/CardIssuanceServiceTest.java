/*package com.CardMaster.service.cias;

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
import com.CardMaster.security.iam.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardIssuanceServiceTest {

    @Mock private CardRepository cardRepository;
    @Mock private CardAccountRepository accountRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private CardProductRepository productRepository;
    @Mock private JwtUtil jwtUtil;

    @InjectMocks private CardIssuanceService cardIssuanceService;

    @Test
    void issueCard_success() {
        // Arrange
        Long customerId = 1L;
        Long productId = 100L;
        Double creditLimit = 50000.0;
        String token = "Bearer dummyToken";

        Customer customer = Customer.builder()
                .customerId(customerId)
                .name("John Doe")
                .income(50000.0)
                .build();

        CardProduct product = CardProduct.builder()
                .productId(productId)
                .name("Platinum")
                .interestRate(15.0)
                .build();

        Card card = Card.builder()
                .cardId(10L)
                .customer(customer)
                .product(product)
                .maskedCardNumber("XXXX-XXXX-XXXX-1234")
                .expiryDate(LocalDate.now().plusYears(5))
                .cvvHash("hash123")
                .status(CardStatus.ISSUED)
                .build();

        CardAccount account = CardAccount.builder()
                .accountId(20L)
                .card(card)
                .creditLimit(creditLimit)
                .availableLimit(creditLimit)
                .openDate(LocalDate.now())
                .status(AccountStatus.ACTIVE)
                .build();

        when(jwtUtil.extractUsername("dummyToken")).thenReturn("user");
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        when(accountRepository.save(any(CardAccount.class))).thenReturn(account);

        // Act
        CardAccount result = cardIssuanceService.issueCard(customerId, productId, creditLimit, token);

        // Assert
        assertNotNull(result);
        assertEquals(20L, result.getAccountId());
        assertEquals(AccountStatus.ACTIVE, result.getStatus());
        assertEquals("XXXX-XXXX-XXXX-1234", result.getCard().getMaskedCardNumber());

        verify(cardRepository, times(1)).save(any(Card.class));
        verify(accountRepository, times(1)).save(any(CardAccount.class));
        verify(jwtUtil).extractUsername("dummyToken");
    }

    @Test
    void issueCard_invalidCreditLimit_throwsException() {
        assertThrows(CardIssuanceException.class,
                () -> cardIssuanceService.issueCard(1L, 100L, -500.0, "Bearer token"));
    }

    @Test
    void issueCard_customerNotFound_throwsException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CardIssuanceException.class,
                () -> cardIssuanceService.issueCard(1L, 100L, 5000.0, "Bearer token"));
    }

    @Test
    void issueCard_productNotFound_throwsException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(Customer.builder().customerId(1L).build()));
        when(productRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(CardIssuanceException.class,
                () -> cardIssuanceService.issueCard(1L, 100L, 5000.0, "Bearer token"));
    }

    @Test
    void getAllCards_returnsList() {
        when(cardRepository.findAll()).thenReturn(List.of(new Card(), new Card()));
        assertEquals(2, cardIssuanceService.getAllCards().size());
        verify(cardRepository).findAll();
    }

    @Test
    void getAllAccounts_returnsList() {
        when(accountRepository.findAll()).thenReturn(List.of(new CardAccount()));
        assertEquals(1, cardIssuanceService.getAllAccounts().size());
        verify(accountRepository).findAll();
    }
}
*/