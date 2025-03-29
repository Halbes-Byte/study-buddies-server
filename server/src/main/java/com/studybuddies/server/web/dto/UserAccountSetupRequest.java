package com.studybuddies.server.web.dto;

import com.studybuddies.server.web.dto.interfaces.CreationRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Setter;

@Setter
public class UserAccountSetupRequest implements CreationRequest {
  @NotBlank
  public String username;

  // Todo: Add Modules here and in the UserEntity. Then write a mapstruct mapping function.
}
