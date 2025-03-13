package com.studybuddies.server.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "meeting")
@Entity
@Getter
@Setter
@Builder // debatable, makes testing easier
@AllArgsConstructor
@NoArgsConstructor
public class MeetingEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  String title;

  String description;

  @Column(nullable = false)
  LocalDateTime date_from;
  @Column(nullable = false)
  LocalDateTime date_until;
  Repeat repeatable;
  String place;

  //@ManyToOne
  //private UserEntity creator; // retrieve creator in Mapping function via JWT Token
}
