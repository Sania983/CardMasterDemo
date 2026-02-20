package com.CardMaster.mapper.cias;

import com.CardMaster.Enum.cias.CardStatus;
import com.CardMaster.dto.cias.CardRequestDto;
import com.CardMaster.dto.cias.CardResponseDto;
import com.CardMaster.model.cias.Card;
import com.CardMaster.model.cpl.CardProduct;
import com.CardMaster.model.paa.Customer;
import com.CardMaster.dao.paa.CustomerRepository;
import com.CardMaster.dao.cpl.CardProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardMapper {

    private final CustomerRepository customerRepository;
    private final CardProductRepository productRepository;

    public Card toEntity(CardRequestDto dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + dto.getCustomerId()));

        CardProduct product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + dto.getProductId()));

        Card card = new Card();
        card.setCustomer(customer);
        card.setProduct(product);
        card.setMaskedCardNumber(dto.getMaskedCardNumber());
        card.setExpiryDate(dto.getExpiryDate());
        card.setCvvHash(dto.getCvvHash());
        card.setStatus(CardStatus.valueOf(dto.getStatus().toUpperCase()));
        return card;
    }

    public CardResponseDto toDTO(Card card) {
        CardResponseDto dto = new CardResponseDto();
        dto.setCardId(card.getCardId());
        dto.setCustomerId(card.getCustomer().getCustomerId());
        dto.setProductId(card.getProduct().getProductId());
        dto.setMaskedCardNumber(card.getMaskedCardNumber());
        dto.setExpiryDate(card.getExpiryDate());
        dto.setStatus(card.getStatus().name());
        return dto;
    }
}
