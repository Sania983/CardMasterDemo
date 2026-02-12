package com.CardMaster.model.paa;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
public class CardApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private Long productId;
    private Double requestedLimit;
    private LocalDate applicationDate;

    @Enumerated(EnumType.STRING)
    private CardApplicationStatus status;

    // Enum defined inside the same file
    public enum CardApplicationStatus {
        Submitted,
        UnderReview,
        Approved,
        Rejected
    }
}
