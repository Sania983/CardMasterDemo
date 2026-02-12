package com.CardMaster.model.iam;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String action;
    private String resource;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime timestamp;
    private String metadata;
}