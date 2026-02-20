
package com.CardMaster.dto.cau;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditScoreGenerateRequest {
    private Long applicationId;
    private Integer bureauScore;
}