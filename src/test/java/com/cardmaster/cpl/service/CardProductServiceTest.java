package com.cardmaster.cpl.service;

import com.CardMaster.dao.cpl.CardProductRepository;
import com.CardMaster.dto.cpl.CardProductRequestDto;
import com.CardMaster.dto.cpl.CardProductResponseDto;
import com.CardMaster.mapper.cpl.CardProductMapper;
import com.CardMaster.model.cpl.CardProduct;
import com.CardMaster.service.cpl.CardProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CardProductService.
 * Mocks the repository and mapper. No Spring context required.
 */
@ExtendWith(MockitoExtension.class)
class CardProductServiceTest {

    @Mock
    private CardProductRepository repository;

    @Mock
    private CardProductMapper mapper;

    @InjectMocks
    private CardProductService service;

    private CardProductRequestDto requestDto;
    private CardProduct entityBeforeSave;
    private CardProduct entitySaved;
    private CardProductResponseDto responseDto;

    @BeforeEach
    void setUp() {
        // ----- build request -----
        requestDto = new CardProductRequestDto();
        requestDto.setName("Platinum Plus");
        requestDto.setCategory("PLATINUM");
        requestDto.setInterestRate(new BigDecimal("2.75"));
        requestDto.setAnnualFee(new BigDecimal("2999"));
        requestDto.setStatus("ACTIVE");

        // ----- entity mapping (pre-save & post-save) -----
        entityBeforeSave = new CardProduct();
        entityBeforeSave.setName("Platinum Plus");
        entityBeforeSave.setCategory("PLATINUM");
        entityBeforeSave.setInterestRate(new BigDecimal("2.75"));
        entityBeforeSave.setAnnualFee(new BigDecimal("2999"));
        entityBeforeSave.setStatus("ACTIVE");

        entitySaved = new CardProduct();
        entitySaved.setProductId(1L);
        entitySaved.setName("Platinum Plus");
        entitySaved.setCategory("PLATINUM");
        entitySaved.setInterestRate(new BigDecimal("2.75"));
        entitySaved.setAnnualFee(new BigDecimal("2999"));
        entitySaved.setStatus("ACTIVE");

        // ----- response mapping -----
        responseDto = new CardProductResponseDto();
        responseDto.setProductId(1L);
        responseDto.setName("Platinum Plus");
        responseDto.setCategory("PLATINUM");
        responseDto.setInterestRate(new BigDecimal("2.75"));
        responseDto.setAnnualFee(new BigDecimal("2999"));
        responseDto.setStatus("ACTIVE");
    }

    @Test
    @DisplayName("create(): maps request -> entity, saves, maps entity -> response")
    void create_success() {
        // mapper.toEntity(request) -> entityBeforeSave
        when(mapper.toEntity(requestDto)).thenReturn(entityBeforeSave);
        // repository.save(entityBeforeSave) -> entitySaved
        when(repository.save(entityBeforeSave)).thenReturn(entitySaved);
        // mapper.toResponse(entitySaved) -> responseDto
        when(mapper.toResponse(entitySaved)).thenReturn(responseDto);

        CardProductResponseDto result = service.create(requestDto);

        assertThat(result).isNotNull();
        assertThat(result.getProductId().equals(1L);
        assertThat(result.getName()).isEqualTo("Platinum Plus");

        // verify interactions & captured argument to save
        ArgumentCaptor<CardProduct> entityCaptor = ArgumentCaptor.forClass(CardProduct.class);
        verify(mapper).toEntity(requestDto);
        verify(repository).save(entityCaptor.capture());
        verify(mapper).toResponse(entitySaved);

        CardProduct savedArg = entityCaptor.getValue();
        assertThat(savedArg.getName()).isEqualTo("Platinum Plus");
        assertThat(savedArg.getAnnualFee()).isEqualTo(new BigDecimal("2999"));
        verifyNoMoreInteractions(mapper, repository);
    }

    @Test
    @DisplayName("findAll(): returns mapped list")
    void findAll_success() {
        when(repository.findAll()).thenReturn(List.of(entitySaved));
        when(mapper.toResponse(entitySaved)).thenReturn(responseDto);

        List<CardProductResponseDto> list = service.findAll();

        assertThat(list).hasSize(1);
        assertThat(list.getFirst().getProductId().equals(1L);

        verify(repository).findAll();
        verify(mapper).toResponse(entitySaved);
    }
}