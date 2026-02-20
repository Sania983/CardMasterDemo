
package com.CardMaster.dto.cau;

import lombok.Data;

@Data
public class CreditScoreGenerateRequest {
    private Long applicationId;
    private Integer bureauScore;
}