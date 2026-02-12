package com.CardMaster.model.cias;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "card")
@Data
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    private Long customerId;
    private Long productId;
    private String maskedCardNumber;
    private LocalDate expiryDate;
    private String cvvHash;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        Active,
        Blocked,
        Issued
    }
}
