package com.studybuddies.server.web.dto;

import com.studybuddies.server.web.dto.interfaces.Responses;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class StudyGroupResponse implements Responses {
  String meetingId;
  String userId;
}
