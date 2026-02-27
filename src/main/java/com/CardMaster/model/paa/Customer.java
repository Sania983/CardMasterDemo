package com.CardMaster.model.paa;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "customers")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private LocalDate dob;


    @Embedded
    private ContactInfo_Customer contactInfo;

    @Column(nullable = false)
    private Double income;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentType employmentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatus status;

    // Cascade ensures applications are persisted/removed with customer
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<CardApplication> applications;

    public enum EmploymentType {
        Salaried, SelfEmployed, Student, Retired, Unemployed
    }
    public enum CustomerStatus {
        Active, Inactive, Suspended, Closed
    }

}
