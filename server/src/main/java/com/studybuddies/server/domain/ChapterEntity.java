package com.studybuddies.server.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
public class ChapterEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;

  @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CheckboxEntity> checkbox = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "module_id")
  private UserModule module;
}
