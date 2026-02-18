package com.CardMaster.model.cpl;

import com.CardMaster.Enum.cpl.CardCategory;
import com.CardMaster.Enum.cpl.ProductStatus;
import jakarta.persistence.*;
import jdk.jfr.DataAmount;

import java.math.BigDecimal;

@Entity
@Table(name = "card_product")
@Access(AccessType.FIELD)
public class CardProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long productId;

    @Column(nullable = false, unique = true, length = 80)
    String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    CardCategory category;

    @Column(nullable = false, precision = 5, scale = 2)
    BigDecimal interestRate;

    @Column(nullable = false, precision = 12, scale = 2)
    BigDecimal annualFee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    ProductStatus status;

    protected CardProduct() {}

    // IMPORTANT: 6-arg constructor (includes annualFee)
    public CardProduct(Long productId,
                       String name,
                       CardCategory category,
                       BigDecimal interestRate,
                       BigDecimal annualFee,
                       ProductStatus status) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.interestRate = interestRate;
        this.annualFee = annualFee;
        this.status = status;
    }

    // Non-bean, read-only accessors for mapper in another package
    public Long productId()           { return productId; }
    public String name()              { return name; }
    public CardCategory category()    { return category; }
    public BigDecimal interestRate()  { return interestRate; }
    public BigDecimal annualFee()     { return annualFee; }
    public ProductStatus status()     { return status; }
}