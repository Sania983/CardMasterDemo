package com.CardMaster.dto.cpl.request;

import com.CardMaster.Enum.cpl.FeeType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeeConfigRequest {

    @NotNull
    private Long productId;

    @NotNull
    private FeeType feeType;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal amount;
}





