package com.CardMaster.mapper.cias;

import com.CardMaster.Enum.cias.CardStatus;
import com.CardMaster.dao.cpl.CardProductRepository;
import com.CardMaster.dao.paa.CustomerRepository;
import com.CardMaster.dto.cias.CardRequestDto;
import com.CardMaster.dto.cias.CardResponseDto;
import com.CardMaster.model.cias.Card;
import com.CardMaster.model.cpl.CardProduct;
import com.CardMaster.model.paa.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardMapperTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CardProductRepository productRepository;

    @InjectMocks
    private CardMapper cardMapper;

    private CardRequestDto requestDto;
    private Customer customer;
    private CardProduct product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDto = new CardRequestDto();
        requestDto.setCustomerId(1L);
        requestDto.setProductId(100L);
        requestDto.setMaskedCardNumber("**** **** **** 1234");
        requestDto.setExpiryDate(LocalDate.of(2030, 12, 31));
        requestDto.setCvvHash("hashed-cvv");

        customer = new Customer();
        customer.setCustomerId(1L);

        product = new CardProduct();
        product.setProductId(100L);
    }

    @Test
    void testToEntity() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));

        Card card = cardMapper.toEntity(requestDto);

        assertNotNull(card);
        assertEquals(customer, card.getCustomer());
        assertEquals(product, card.getProduct());
        assertEquals("**** **** **** 1234", card.getMaskedCardNumber());
        assertEquals(CardStatus.ISSUED, card.getStatus());
    }

    @Test
    void testToDTO() {
        Card card = new Card();
        card.setCardId(1L);
        card.setCustomer(customer);
        card.setProduct(product);
        card.setMaskedCardNumber("**** **** **** 1234");
        card.setExpiryDate(LocalDate.of(2030, 12, 31));
        card.setStatus(CardStatus.ACTIVE);

        CardResponseDto dto = cardMapper.toDTO(card);

        assertEquals(1L, dto.getCardId());
        assertEquals(1L, dto.getCustomerId());
        assertEquals(100L, dto.getProductId());
        assertEquals("**** **** **** 1234", dto.getMaskedCardNumber());
        assertEquals("ACTIVE", dto.getStatus());
    }
}
