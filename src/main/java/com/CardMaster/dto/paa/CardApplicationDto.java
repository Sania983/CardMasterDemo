package com.CardMaster.dto.paa;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CardApplicationDto {

        private Long applicationId;
        private Long customerId;          // Reference to Customer
        private Long productId;
        private Double requestedLimit;
        private LocalDate applicationDate;
        private String status;            // Submitted, UnderReview, Approved, Rejected
    }


