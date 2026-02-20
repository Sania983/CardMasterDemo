package com.CardMaster.dto.cpl.response;

import com.CardMaster.model.cpl.CardProduct;
import com.CardMaster.Enum.cpl.FeeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeConfigResponse {

    private long feeId;

    private CardProduct product;

    private FeeType feeType;
}