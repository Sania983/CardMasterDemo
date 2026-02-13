package com.CardMaster.model.cau;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user")              // <-- must be "user" to match the FK
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")      // <-- must be "user_id" to match the FK
    private Long userId;

    @Column(name = "name")
    private String name;
}