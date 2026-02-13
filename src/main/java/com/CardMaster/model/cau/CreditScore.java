
package com.CardMaster.model.cau;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class CreditScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scoreId;

    // ApplicationID → relationship to CardApplication
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private CardApplication application;

    private Integer bureauScore;          // BureauScore   izant_com/Documents/Microsoft%20Copilot%20Chat%20Files/CardMaster%20-%20Credit%20Card%20Issuance,%20Transactions%20%26%20Risk%20Management%20System.pdf)
    private Integer internalScore;        // InternalScore  [1](https://cognizantonline-my.sharepoint.com/personal/2465987_cognizant_com/Documents/Microsoft%20Copilot%20Chat%20Files/CardMaster%20-%20Credit%20Card%20Issuance,%20Transactions%20%26%20Risk%20Management%20System.pdf)
    private LocalDateTime generatedDate;  // GeneratedDate  [1](https://cognizantonline-my.sharepoint.com/personal/2465987_cognizant_com/Documents/Microsoft%20Copilot%20Chat%20Files/CardMaster%20-%20Credit%20Card%20Issuance,%20Transactions%20%26%20Risk%20Management%20System.pdf)
}