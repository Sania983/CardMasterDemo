package com.CardMaster.dto.cpl.response;

import com.CardMaster.Enum.cpl.FeeType;
import java.math.BigDecimal;

public record FeeConfigResponse(
        Long feeId,
        Long productId,
        FeeType feeType,
        BigDecimal amount
) {}