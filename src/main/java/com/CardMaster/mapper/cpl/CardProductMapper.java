package com.CardMaster.mapper.cpl;

import com.CardMaster.model.cpl.CardProduct;
import com.CardMaster.dto.cpl.request.CardProductCreateRequest;
import com.CardMaster.dto.cpl.request.CardProductUpdateRequest;
import com.CardMaster.dto.cpl.response.CardProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CardProductMapper {

    // Create -> Entity (DB generates ID)
    default CardProduct toEntity(CardProductCreateRequest dto) {
        return new CardProduct(
                null,
                dto.name(),
                dto.category(),
                dto.interestRate(),
                dto.annualFee(),
                dto.status()
        );
    }

    // Update -> Entity (dto carries productId)
    default CardProduct toEntity(CardProductUpdateRequest dto) {
        return new CardProduct(
                dto.productId(),
                dto.name(),
                dto.category(),
                dto.interestRate(),
                dto.annualFee(),
                dto.status()
        );
    }

    // Entity -> Response (use non-bean accessors via expressions)
    @Mapping(target = "productId",    expression = "java(e.productId())")
    @Mapping(target = "name",         expression = "java(e.name())")
    @Mapping(target = "category",     expression = "java(e.category())")
    @Mapping(target = "interestRate", expression = "java(e.interestRate())")
    @Mapping(target = "annualFee",    expression = "java(e.annualFee())")
    @Mapping(target = "status",       expression = "java(e.status())")
    CardProductResponse toResponse(CardProduct e);

    List<CardProductResponse> toResponses(List<CardProduct> entities);
}