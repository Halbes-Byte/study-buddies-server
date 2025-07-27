package com.studybuddies.server.web.dto.user;

import com.studybuddies.server.web.dto.interfaces.CreationRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Setter;

@Setter
public class UserAccountSetupRequest implements CreationRequest {

  @NotBlank
  public String username;
}
