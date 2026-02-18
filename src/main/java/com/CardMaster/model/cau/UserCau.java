package com.CardMaster.model.cau;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "underwriter")
public class UserCau {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "underwriter_id")
    private Long userId;

    private String name;

    // Cascade ensures decisions are persisted/removed with user
    @OneToMany(mappedBy = "underwriter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UnderwritingDecision> decisions;
}
