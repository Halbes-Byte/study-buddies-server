package com.studybuddies.server.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class ModuleEntity {
  @Id
  @Size(min = 5, max = 50, message = "Please provide the entire module name")
  @Column(nullable = false, unique = true)
  String name;
}
