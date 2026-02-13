// com/CardMaster/model/CardApplication.java
package com.CardMaster.model.cau;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CardApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    // minimal extra field so it's not empty (optional)
    private Double requestedLimit;
}