package com.CardMaster.dto.cpl.request;

import com.CardMaster.Enum.cpl.CardCategory;
import com.CardMaster.Enum.cpl.ProductStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardProductCreateRequest {

    @NotBlank
    private String name;

    @NotNull
    private CardCategory category;

    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal interestRate;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal annualFee;

    @NotNull
    private ProductStatus status;
}