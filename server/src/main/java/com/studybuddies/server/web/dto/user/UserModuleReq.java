package com.studybuddies.server.web.dto.user;

import com.studybuddies.server.web.dto.chapter.ChapterCreationRequest;
import lombok.Getter;

@Getter
public class UserModuleReq {
  private String name;
  private String examDate;
  private String examLoc;
  private String examTime;
  private ChapterCreationRequest[] chapter;
}
