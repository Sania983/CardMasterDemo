package com.CardMaster.mapper.billing;

import com.CardMaster.dto.billing.StatementRequestDto;
import com.CardMaster.dto.billing.StatementResponseDto;
import com.CardMaster.model.billing.Statement;
import com.CardMaster.model.billing.StatementStatus;
import org.springframework.stereotype.Component;

@Component
public class StatementMapper {

    public Statement toEntity(StatementRequestDto dto) {
        Statement st = new Statement();
        st.setAccountId(dto.getAccountId());
        st.setPeriodStart(dto.getPeriodStart());
        st.setPeriodEnd(dto.getPeriodEnd());
        st.setTotalDue(dto.getTotalDue());
        st.setMinimumDue(dto.getMinimumDue());
        st.setGeneratedDate(dto.getGeneratedDate());
        st.setStatus(StatementStatus.valueOf(dto.getStatus().name()));
        return st;
    }

    public StatementResponseDto toDTO(Statement st) {
        StatementResponseDto dto = new StatementResponseDto();
        dto.setStatementId(st.getStatementId());
        dto.setAccountId(st.getAccountId());
        dto.setPeriodStart(st.getPeriodStart());
        dto.setPeriodEnd(st.getPeriodEnd());
        dto.setTotalDue(st.getTotalDue());
        dto.setMinimumDue(st.getMinimumDue());
        dto.setGeneratedDate(st.getGeneratedDate());
        dto.setStatus(st.getStatus());
        return dto;
    }
}
