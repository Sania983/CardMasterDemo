package com.CardMaster.dto.cpl.request;

import com.CardMaster.Enum.cpl.CardCategory;
import com.CardMaster.Enum.cpl.ProductStatus;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CardProductCreateRequest(
        @NotBlank String name,
        @NotNull  CardCategory category,
        @DecimalMin("0.00") @DecimalMax("100.00") BigDecimal interestRate,
        @DecimalMin("0.00") BigDecimal annualFee,
        @NotNull  ProductStatus status
) {}