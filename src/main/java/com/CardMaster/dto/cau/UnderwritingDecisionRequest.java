// com/cardMaster/dto/UnderwritingDecisionRequest.java
package com.CardMaster.dto.cau;

import com.CardMaster.Enum.cau.UnderwritingDecisionType;
import lombok.Data;

@Data
public class UnderwritingDecisionRequest {
    private Long applicationId;
    private Long underwriterId;
    private UnderwritingDecisionType decision; // optional: auto if null
    private Double approvedLimit;              // optional
    private String remarks;
}