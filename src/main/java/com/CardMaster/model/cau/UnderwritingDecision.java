
package com.CardMaster.model.cau;

import com.CardMaster.Enum.cau.UnderwritingDecisionType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class UnderwritingDecision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long decisionId;

    // PDF: ApplicationID → relationship to CardApplication
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private CardApplication application;  // ApplicationID
    // PDF: UnderwriterID → relationship to User
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "underwriter_id", nullable = false)
    private User underwriter;             // UnderwriterID
    @Enumerated(EnumType.STRING)
    private UnderwritingDecisionType decision; // Approve/Reject/Conditional
    private Double approvedLimit;         // ApprovedLimit
    private String remarks;               // Remarks
    private LocalDateTime decisionDate;   // DecisionDate
}
