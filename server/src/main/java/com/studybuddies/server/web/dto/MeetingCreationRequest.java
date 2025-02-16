package com.studybuddies.server.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Setter;

@Setter
public class MeetingCreationRequest {
  @NotBlank
  public String title;
  public String description;
  @NotBlank
  public String date_from;
  @NotBlank
  public String date_until;
  public String repeatable;
  public String place;
}
