package com.CardMaster.dto.bsp;

import com.CardMaster.model.billing.StatementStatus;
import lombok.Data;
import java.time.LocalDate;

@Data
public class StatementRequestDto {
    private Long accountId;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private Double totalDue;
    private Double minimumDue;
    private LocalDate generatedDate;
    private StatementStatus status;
}

@Data
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
