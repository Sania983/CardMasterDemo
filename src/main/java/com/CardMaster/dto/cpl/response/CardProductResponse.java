package com.CardMaster.dto.cpl.response;

import com.CardMaster.Enum.cpl.CardCategory;
import com.CardMaster.Enum.cpl.ProductStatus;
import java.math.BigDecimal;

public record CardProductResponse(
        Long productId,
        String name,
        CardCategory category,
        BigDecimal interestRate,
        BigDecimal annualFee,
        ProductStatus status
) {}