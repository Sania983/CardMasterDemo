package com.CardMaster.Mapper.cias;

import com.CardMaster.dao.cpl.CardProductRepository;
import com.CardMaster.dao.paa.CustomerRepository;
import com.CardMaster.dto.cias.CardRequestDto;
import com.CardMaster.mapper.cias.CardMapper;
import com.CardMaster.model.cias.Card;
import com.CardMaster.model.cpl.CardProduct;
import com.CardMaster.model.paa.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardMapperTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CardProductRepository productRepository;

    @InjectMocks
    private CardMapper cardMapper;

    @Test
    void testToEntity_success() {
        CardRequestDto dto = new CardRequestDto(
                1L,
                2L,
                "XXXX-XXXX-1234",
                LocalDate.now().plusYears(5),
                "hash123",
                "ISSUED"
        );

        Customer customer = new Customer();
        customer.setCustomerId(1L);

        CardProduct product = new CardProduct();
        product.setProductId(2L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product));

        Card card = cardMapper.toEntity(dto);

        assertNotNull(card);
        assertEquals("XXXX-XXXX-1234", card.getMaskedCardNumber());
        assertEquals(customer, card.getCustomer());
        assertEquals(product, card.getProduct());
    }
}
