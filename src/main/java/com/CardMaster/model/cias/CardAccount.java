package com.CardMaster.model.cias;

import com.CardMaster.model.cias.Card;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "card_accounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @OneToOne(optional = false)
    @JoinColumn(name = "card_id", nullable = false, unique = true)
    private Card card;

    @Positive
    @Column(name = "credit_limit", nullable = false)
    private Double creditLimit;

    @PositiveOrZero
    @Column(name = "available_limit", nullable = false)
    private Double availableLimit;

    @NotNull
    @Column(name = "open_date", nullable = false)
    private LocalDate openDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CardAccount status;  // ACTIVE, CLOSED
}
