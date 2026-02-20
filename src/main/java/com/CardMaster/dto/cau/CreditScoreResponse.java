
package com.CardMaster.dto.cau;

import lombok.Data;

@Data
public class CreditScoreResponse {
    private Long scoreId;
    private Long applicationId;
    private Integer bureauScore;
    private Integer internalScore;
    private String generatedDate;
}