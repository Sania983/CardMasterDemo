package com.CardMaster.mapper.cpl;

import com.CardMaster.model.cpl.CardProduct;
import com.CardMaster.model.cpl.FeeConfig;
import com.CardMaster.dto.cpl.request.FeeConfigRequest;
import com.CardMaster.dto.cpl.response.FeeConfigResponse;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FeeConfigMapper {

    // Build a new FeeConfig using the request + the already-loaded CardProduct
    @ObjectFactory
    default FeeConfig create(FeeConfigRequest dto, @Context CardProduct product) {
        // constructor: (Long feeId, CardProduct product, FeeType feeType, BigDecimal amount)
        return new FeeConfig(null, product, dto.feeType(), dto.amount());
    }

    // Entity -> DTO using expressions (since mapper is in another package and we use non-bean accessors)
    @Mapping(target = "feeId",     expression = "java(e.feeId())")
    @Mapping(target = "productId", expression = "java(e.product().productId())")
    @Mapping(target = "feeType",   expression = "java(e.feeType())")
    @Mapping(target = "amount",    expression = "java(e.amount())")
    FeeConfigResponse toResponse(FeeConfig e);

    List<FeeConfigResponse> toResponses(List<FeeConfig> entities);
}