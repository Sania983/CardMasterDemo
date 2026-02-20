
package com.CardMaster.dto.cau;


import com.CardMaster.Enum.cau.UnderwritingDecisionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnderwritingDecisionRequest {
    @NotNull
    private Long underwriterId;
    private UnderwritingDecisionType decision;
    @Positive// optional: auto if null
    private Double approvedLimit;// optional
    private String remarks;
}



