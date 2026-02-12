package com.CardMaster.model.paa;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private CardApplication application;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    private String fileURI;
    private LocalDate uploadedDate;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    // Enum for document type
    public enum DocumentType {
        IdentityProof,
        AddressProof,
        IncomeProof,
        EmploymentProof,
        Other
    }

    // Enum for document status
    public enum DocumentStatus {
        Submitted,
        NotSubmitted
    }
}
