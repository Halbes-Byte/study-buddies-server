package com.studybuddies.server.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserModule {

  @Id
  @GeneratedValue
  private Long id;

  private String name;
  private String examDate;
  private String examLoc;

  @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ChapterEntity> chapter;

  @ManyToOne
  @JoinColumn(name = "user_uuid")
  private UserEntity user;
}
