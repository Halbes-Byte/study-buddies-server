package com.studybuddies.server.web.dto.meeting;

import com.studybuddies.server.domain.ChangeType;
import com.studybuddies.server.web.dto.interfaces.ChangeRequest;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MeetingChangeRequest implements ChangeRequest {

  public ChangeType changeType;
  public String module;
  public String description;
  public String dateFrom;
  public String dateUntil;
  public String repeatable;
  public String place;
}
