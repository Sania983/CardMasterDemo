package com.CardMaster.model.paa;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "card_application")
public class CardApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private Long productId;
    private Double requestedLimit;
    private LocalDate applicationDate;

    @Enumerated(EnumType.STRING)
    private CardApplicationStatus status;

    // Cascade ensures documents are persisted/removed with application
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents;

    public enum CardApplicationStatus {
        Submitted, UnderReview, Approved, Rejected
    }
}
