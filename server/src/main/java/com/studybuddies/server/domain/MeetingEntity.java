package com.studybuddies.server.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "meeting")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingEntity {

@Id
@GeneratedValue(strategy = GenerationType.UUID)
UUID id;

UUID superId;

@Column(nullable = false)
@NotBlank
String module;

String description;

@Column(nullable = false)
LocalDateTime dateFrom;
@Column(nullable = false)
LocalDateTime dateUntil;
Repeat repeatable;
String place;

@ManyToOne
private UserEntity creator;
}
