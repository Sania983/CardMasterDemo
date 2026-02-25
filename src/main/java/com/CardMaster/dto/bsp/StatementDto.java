package com.CardMaster.dto.bsp;

import com.CardMaster.Enum.bsp.StatementStatus;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatementResponseDto {
    private Long statementId;
    private Long accountId;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private Double totalDue;
    private Double minimumDue;
    private LocalDate generatedDate;
    private StatementStatus status;
}
