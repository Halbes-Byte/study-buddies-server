package com.studybuddies.server.web.mapper;

import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.domain.UserEntity;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class StudyGroupMapperUtils {

  @Named("userEntityToId")
  public String userEntityToId(UserEntity user) {
    return user.getUsername();
  }

  @Named("meetingEntityToId")
  public String meetingEntityToId(MeetingEntity meeting) {
    return meeting.getId().toString();
  }
}
