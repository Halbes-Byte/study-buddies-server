package com.studybuddies.server.web.dto.module;

import com.studybuddies.server.web.dto.interfaces.Responses;
import lombok.Getter;

@Getter
public class ModuleResponse implements Responses {
  public String name;
}
