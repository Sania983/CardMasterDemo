package com.CardMaster.model.cpl;

import com.CardMaster.Enum.cpl.CardCategory;
import com.CardMaster.Enum.cpl.ProductStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Data
@Table(name = "card_products")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId; // Use Long (wrapper) for nullability with JPA

    @NotBlank
    @Column(nullable = false, length = 80)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardCategory category;

    @Positive
    @Column(nullable = false)
    private Double interestRate;

    @Positive
    @Column(nullable = false)
    private Double annualFee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private ProductStatus status;
}

