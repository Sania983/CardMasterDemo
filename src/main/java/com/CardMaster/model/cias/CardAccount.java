package com.CardMaster.model.cias;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "cardaccount")
@Data
public class CardAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    private Long cardId;
    private Double creditLimit;
    private Double availableLimit;
    private LocalDate openDate;

    @Enumerated(EnumType.STRING)
    private Status status;


    public enum Status {
        Active,
        Closed
    }
}
