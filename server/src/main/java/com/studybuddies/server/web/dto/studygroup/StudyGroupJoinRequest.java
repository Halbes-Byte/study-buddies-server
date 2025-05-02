package com.studybuddies.server.web.dto.studygroup;

import com.studybuddies.server.web.dto.interfaces.CreationRequest;
import jakarta.validation.constraints.NotBlank;

public class StudyGroupJoinRequest implements CreationRequest {

  @NotBlank
  public String meetingId;
  // User ID given through Request
}
