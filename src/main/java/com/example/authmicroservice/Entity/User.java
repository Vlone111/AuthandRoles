package com.example.authmicroservice.Entity;


import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    //@Valid нужно ли это хз
    private String email;

    @Column(nullable = false)
    private Roles role;


    @Column(name = "Created_at", nullable = false)
    private LocalDateTime localDateTime;

    @PrePersist
    public void prePersist() {
        this.localDateTime = LocalDateTime.now();
    }


}
