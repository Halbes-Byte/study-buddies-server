package com.studybuddies.server.services;


import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.persistance.MeetingRepository;
import com.studybuddies.server.web.dto.MeetingChangeRequest;
import com.studybuddies.server.web.dto.MeetingCreationRequest;
import com.studybuddies.server.web.mapper.MeetingMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MeetingService {

  private final MeetingMapper meetingMapper;
  private final MeetingRepository meetingRepository;

  @Transactional
  public void saveMeeting(MeetingCreationRequest mcr) {
    MeetingEntity meetingEntity = meetingMapper.MeetingCreationRequestToMeetingEntity(mcr);
    meetingRepository.save(meetingEntity);
  }

  @Transactional
  public void changeMeetingInDatabase(long id, MeetingChangeRequest meetingChangeRequest) {
    Optional<MeetingEntity> requestResult = meetingRepository.findById(id);

    requestResult.ifPresent(meetingEntity -> {
      MeetingEntity changedMeeting = meetingMapper.MeetingChangeRequestToMeetingEntity(meetingChangeRequest);

      meetingEntity.setTitle(changedMeeting.getTitle());
      meetingEntity.setDescription(changedMeeting.getDescription());
      meetingEntity.setLinks(changedMeeting.getLinks());
      meetingEntity.setDate_from(changedMeeting.getDate_from());
      meetingEntity.setDate_until(changedMeeting.getDate_until());
      meetingEntity.setRepeatable(changedMeeting.getRepeatable());
      meetingEntity.setPlace(changedMeeting.getPlace());

      meetingRepository.save(meetingEntity);
    });
  }
}
