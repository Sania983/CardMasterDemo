package com.CardMaster.model;
import com.CardMaster.Enum.UserEnum;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String name;
    @Enumerated(EnumType.STRING)
    // Store enum as string in DB
    private UserEnum role;
    private String email;
    private String phone;
}