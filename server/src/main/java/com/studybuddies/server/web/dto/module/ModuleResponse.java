package com.studybuddies.server.web.dto.module;

import com.studybuddies.server.domain.ChapterEntity;
import com.studybuddies.server.web.dto.interfaces.Responses;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ModuleResponse implements Responses {
  private String name;
  private String examDate;
  private String examTime;
  private String examLoc;
  private List<ChapterEntity> chapter;
}
