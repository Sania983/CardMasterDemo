package com.CardMaster.dto.cpl.response;

import com.CardMaster.Enum.cpl.CardCategory;
import com.CardMaster.Enum.cpl.ProductStatus;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardProductResponse {

    private Long productId;
    private String name;
    private CardCategory category;
    private BigDecimal interestRate;
    private BigDecimal annualFee;
    private ProductStatus status;
}