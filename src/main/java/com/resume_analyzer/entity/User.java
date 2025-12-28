package com.resume_analyzer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String apiKey;

    @Column(nullable = false)
    private String email;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private SubscriptionPlan plan;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Usage usage;
}