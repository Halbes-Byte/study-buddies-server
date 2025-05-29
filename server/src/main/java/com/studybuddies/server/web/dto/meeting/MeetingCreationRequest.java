package com.studybuddies.server.web.dto.meeting;

import com.studybuddies.server.web.dto.interfaces.CreationRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Setter;

@Setter
public class MeetingCreationRequest implements CreationRequest {

  @NotBlank
  public String module;
  public String description;
  @NotBlank
  public String dateFrom;
  @NotBlank
  public String dateUntil;
  public String repeatable;
  public String place;
}
