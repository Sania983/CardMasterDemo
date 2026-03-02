package com.CardMaster.service.cias;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardIssuanceServiceTest {

    @Mock private CardRepository cardRepository;
    @Mock private CardAccountRepository accountRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private CardProductRepository productRepository;
    @Mock private JwtUtil jwtUtil;

    @InjectMocks private CardIssuanceService cardIssuanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void issueCard_success() {
        Customer customer = new Customer();
        customer.setCustomerId(1L);

        CardProduct product = new CardProduct();
        product.setProductId(2L);

        Card card = new Card();
        card.setCardId(100L);

        when(jwtUtil.extractUsername(anyString())).thenReturn("testUser");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product));
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        when(accountRepository.save(any(CardAccount.class))).thenAnswer(invocation -> {
            CardAccount acc = invocation.getArgument(0);
            acc.setAccountId(200L);
            return acc;
        });

        CardAccount result = cardIssuanceService.issueCard(1L, 2L, 5000.0, "Bearer token");

        assertNotNull(result);
        assertEquals(200L, result.getAccountId());
        assertEquals(5000.0, result.getCreditLimit());
        assertEquals("ACTIVE", result.getStatus().name());
    }

    @Test
    void issueCard_invalidCreditLimit_throwsException() {
        when(jwtUtil.extractUsername(anyString())).thenReturn("testUser");
        assertThrows(CardIssuanceException.class,
                () -> cardIssuanceService.issueCard(1L, 2L, -100.0, "Bearer token"));
    }

    @Test
    void issueCard_customerNotFound_throwsException() {
        when(jwtUtil.extractUsername(anyString())).thenReturn("testUser");
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CardIssuanceException.class,
                () -> cardIssuanceService.issueCard(1L, 2L, 5000.0, "Bearer token"));
    }

    @Test
    void issueCard_productNotFound_throwsException() {
        Customer customer = new Customer();
        customer.setCustomerId(1L);

        when(jwtUtil.extractUsername(anyString())).thenReturn("testUser");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(CardIssuanceException.class,
                () -> cardIssuanceService.issueCard(1L, 2L, 5000.0, "Bearer token"));
    }
}
