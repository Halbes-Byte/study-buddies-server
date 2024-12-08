package com.studybuddies.server.web.services;

import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.web.dto.MeetingCreationRequest;
import com.studybuddies.server.web.mapper.MeetingMapper;
import com.studybuddies.server.web.repositories.MeetingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MeetingService {

  private final MeetingMapper meetingMapper;
  private final MeetingRepository meetingRepository;

  public void saveMeeting(MeetingCreationRequest mcr) {
    meetingRepository.save(meetingMapper.MeetingCreationRequestToMeetingEntity(mcr));
  }
}
