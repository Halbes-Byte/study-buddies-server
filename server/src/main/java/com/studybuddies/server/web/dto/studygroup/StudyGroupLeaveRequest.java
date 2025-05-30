package com.studybuddies.server.web.dto.studygroup;

import com.studybuddies.server.web.dto.interfaces.ChangeRequest;

public class StudyGroupLeaveRequest implements ChangeRequest {

  public String meetingId;
  public String meetingSuperId;
}
