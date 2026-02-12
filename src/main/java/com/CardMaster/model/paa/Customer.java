package com.CardMaster.model.paa;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    private String name;
    private LocalDate dob;
    private String contactInfo;
    private Double income;

    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;

    @Enumerated(EnumType.STRING)
    private CustomerStatus status;

    // Enum for employment type
    public enum EmploymentType {
        Salaried,
        SelfEmployed,
        Student,
        Retired,
        Unemployed
    }

    // Enum for customer status
    public enum CustomerStatus {
        Active,
        Inactive,
        Suspended,
        Closed
    }
}
