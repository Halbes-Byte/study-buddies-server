package com.studybuddies.server.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
public class UserEntity {

  @Id
  UUID uuid;

  @Column(unique = true, nullable = false)
  String username;

  @ElementCollection
  @CollectionTable(name = "userModules", joinColumns = @JoinColumn(name = "userUuid"))
  @Column(name = "moduleName")
  private List<String> modules;
}