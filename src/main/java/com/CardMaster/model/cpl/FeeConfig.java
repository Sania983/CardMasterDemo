// src/main/java/com/CardMaster/Entity/FeeConfig.java
package com.CardMaster.model.cpl;

import com.CardMaster.Enum.cpl.FeeType;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "fee_config",
        uniqueConstraints = @UniqueConstraint(name = "uk_product_fee", columnNames = {"product_id", "fee_type"}))
@Access(AccessType.FIELD)
public class FeeConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long feeId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    CardProduct product;

    @Enumerated(EnumType.STRING)
    @Column(name = "fee_type", nullable = false, length = 20)
    FeeType feeType;

    @Column(nullable = false, precision = 12, scale = 2)
    BigDecimal amount;

    protected FeeConfig() { }

    // ✅ public 4-arg constructor: MUST match types & order
    public FeeConfig(Long feeId, CardProduct product, FeeType feeType, BigDecimal amount) {
        this.feeId = feeId;
        this.product = product;
        this.feeType = feeType;
        this.amount = amount;
    }

    // ✅ non-bean accessors (mapper in another package uses these)
    public Long feeId()         { return feeId; }
    public CardProduct product(){ return product; }
    public FeeType feeType()    { return feeType; }
    public BigDecimal amount()  { return amount; }
}