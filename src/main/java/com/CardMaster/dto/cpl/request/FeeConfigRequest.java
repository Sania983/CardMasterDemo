package com.CardMaster.dto.cpl.request;

import com.CardMaster.Enum.cpl.FeeType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record FeeConfigRequest(
        @NotNull Long productId,
        @NotNull FeeType feeType,
        @DecimalMin("0.00") BigDecimal amount
) {}




