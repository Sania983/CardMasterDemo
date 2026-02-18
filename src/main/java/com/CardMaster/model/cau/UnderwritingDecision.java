package com.CardMaster.model.cau;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import com.CardMaster.model.paa.CardApplication;
import com.CardMaster.Enum.cau.UnderwritingDecisionType;

@Data
@Entity
@Table(name = "underwriting_decision")
public class UnderwritingDecision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long decisionId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "application_id", nullable = false)
    private CardApplication application;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "underwriter_id", nullable = false)
    private UserCau underwriter;

    @Enumerated(EnumType.STRING)
    private UnderwritingDecisionType decision;

    private Double approvedLimit;
    private String remarks;
    private LocalDateTime decisionDate;
}
