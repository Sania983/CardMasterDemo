package com.CardMaster.model.paa;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate dob;
    @Column(unique = true, nullable = false)
    private String contactInfo;

    @Column(nullable = false)
    private Double income;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentType employmentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatus status;

    // Cascade ensures applications are persisted/removed with customer
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardApplication> applications;

    public enum EmploymentType {
        Salaried, SelfEmployed, Student, Retired, Unemployed
    }

    public enum CustomerStatus {
        Active, Inactive, Suspended, Closed
    }
}
