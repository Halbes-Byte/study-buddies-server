package com.studybuddies.server.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  // not implemented
}