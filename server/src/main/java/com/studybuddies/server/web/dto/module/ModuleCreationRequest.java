package com.studybuddies.server.web.dto.module;

import com.studybuddies.server.web.dto.interfaces.CreationRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModuleCreationRequest implements CreationRequest {
  @NotBlank
  @Size(min = 5, max = 50, message = "Please provide the entire module name")
  public String name;
}
