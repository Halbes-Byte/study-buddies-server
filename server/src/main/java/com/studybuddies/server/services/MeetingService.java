package com.studybuddies.server.services;

import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.persistance.MeetingRepository;
import com.studybuddies.server.services.exceptions.MeetingNotFoundException;
import com.studybuddies.server.web.dto.MeetingChangeRequest;
import com.studybuddies.server.web.dto.MeetingCreationRequest;
import com.studybuddies.server.web.dto.MeetingResponse;
import com.studybuddies.server.web.mapper.MeetingMapper;
import jakarta.transaction.Transactional;
import java.util.function.Consumer;
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

    if(requestResult.isEmpty()) {
      throw new MeetingNotFoundException("");
    }
    MeetingEntity meetingEntity = requestResult.get();
    MeetingEntity changedMeeting = meetingMapper.MeetingChangeRequestToMeetingEntity(meetingChangeRequest);

    setIfNotNull(changedMeeting.getTitle(), meetingEntity::setTitle);
    setIfNotNull(changedMeeting.getLinks(), meetingEntity::setLinks);
    setIfNotNull(changedMeeting.getDescription(), meetingEntity::setDescription);
    setIfNotNull(changedMeeting.getDate_from(), meetingEntity::setDate_from);
    setIfNotNull(changedMeeting.getDate_until(), meetingEntity::setDate_until);
    setIfNotNull(changedMeeting.getPlace(), meetingEntity::setPlace);
    setIfNotNull(changedMeeting.getRepeatable(), meetingEntity::setRepeatable);
    meetingMapper.validate(meetingEntity);
    meetingRepository.save(meetingEntity);
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

  private <T> void setIfNotNull(T value, Consumer<T> setFunc) {
    if(value != null) {
       setFunc.accept(value);
    }
  }
}
