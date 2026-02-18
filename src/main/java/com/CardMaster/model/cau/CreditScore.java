package com.CardMaster.model.cau;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import com.CardMaster.model.paa.CardApplication;

@Data
@Entity
@Table(name = "credit_score")
public class CreditScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scoreId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "application_id", nullable = false)
    private CardApplication application;

    private Integer bureauScore;
    private Integer internalScore;
    private LocalDateTime generatedDate;
}
