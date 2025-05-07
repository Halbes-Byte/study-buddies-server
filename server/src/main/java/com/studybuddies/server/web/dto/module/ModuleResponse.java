package com.studybuddies.server.web.dto.module;

import com.studybuddies.server.web.dto.interfaces.Responses;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ModuleResponse implements Responses {
  public String name;
}
