package com.studybuddies.server.web.dto.studygroup;

import com.studybuddies.server.web.dto.interfaces.CreationRequest;

public class StudyGroupJoinRequest implements CreationRequest {
  public String superMeetingId;
  public String meetingId;

  // User ID given through Request
}
