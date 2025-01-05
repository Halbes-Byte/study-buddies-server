package com.studybuddies.server.services;

import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.persistance.MeetingRepository;
import com.studybuddies.server.services.exceptions.MeetingNotFoundException;
import com.studybuddies.server.web.dto.MeetingChangeRequest;
import com.studybuddies.server.web.dto.MeetingCreationRequest;
import com.studybuddies.server.web.dto.MeetingResponse;
import com.studybuddies.server.web.mapper.MeetingMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MeetingService {

  private final MeetingMapper meetingMapper;
  private final MeetingRepository meetingRepository;

  @Transactional
  public Long saveMeetingToDatabase(MeetingCreationRequest mcr) {
    MeetingEntity meetingEntity = meetingMapper.MeetingCreationRequestToMeetingEntity(mcr);
    return meetingRepository.save(meetingEntity).getId();
  }

  @Transactional
  public void changeMeetingInDatabase(Long id, MeetingChangeRequest meetingChangeRequest) {
    Optional<MeetingEntity> requestResult = meetingRepository.findById(id);

    requestResult.ifPresent(meetingEntity -> {
      MeetingEntity changedMeeting = meetingMapper.MeetingChangeRequestToMeetingEntity(meetingChangeRequest);
      BeanUtils.copyProperties(changedMeeting, meetingEntity, "id");
      meetingRepository.save(meetingEntity);
    });
  }

  @Transactional
  public MeetingResponse retrieveMeetingFromDatabase(Long id) {
    Optional<MeetingEntity> requestResult = meetingRepository.findById(id);

    if(requestResult.isEmpty()) {
      throw new MeetingNotFoundException("");
    }

    return meetingMapper.MeetingEntityToMeetingResponse(requestResult.get());
  }

  @Transactional
  public void deleteMeetingFromDatabase(Long id) {
    meetingRepository.deleteById(id);
  }
}
