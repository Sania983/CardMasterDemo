
package com.CardMaster.dto.cau;


import com.CardMaster.Enum.cau.UnderwritingDecisionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnderwritingDecisionRequest {
    private Long applicationId;
    private Long underwriterId;
    private UnderwritingDecisionType decision; // optional: auto if null
    private Double approvedLimit;              // optional
    private String remarks;
}