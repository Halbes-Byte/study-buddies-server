package com.studybuddies.server.test_rest_api;

import jakarta.persistence.*;

@Entity
public record User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id,
        @Column(nullable = false)
        String username,
        @Column(nullable = false)
        String password
) {}
