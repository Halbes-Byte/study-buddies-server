package com.studybuddies.server.web.dto.chapter;

import com.studybuddies.server.web.dto.interfaces.CreationRequest;
import lombok.Getter;

@Getter
public class ChapterCreationRequest implements CreationRequest {
  public String title;
  public CheckboxCreationRequest[] checkbox;
}
