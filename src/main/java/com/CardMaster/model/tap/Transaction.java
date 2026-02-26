package com.CardMaster.model.tap;

import com.CardMaster.Enum.tap.Channel;
import com.CardMaster.Enum.tap.TransactionStatus;
import com.CardMaster.model.cias.CardAccount;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private CardAccount accountId;

    @NotNull @Positive
    private Double amount;

    @NotBlank
    private String currency;

    @NotBlank
    private String merchant;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Channel channel;

    @NotNull
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TransactionStatus status;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
    private List<TransactionHold> holds;
}
