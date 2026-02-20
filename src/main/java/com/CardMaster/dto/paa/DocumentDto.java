package com.CardMaster.dto.paa;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DocumentDto {
    private Long documentId;
    private Long applicationId;       // Reference to CardApplication
    private String documentType;      // Enum as String (IdentityProof, AddressProof, etc.)
    private String fileURI;
    private LocalDate uploadedDate;
    private String status;            // Enum as String (Submitted, NotSubmitted)
}
