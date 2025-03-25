package com.studybuddies.server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.domain.UserEntity;
import com.studybuddies.server.persistance.MeetingRepository;
import com.studybuddies.server.services.exceptions.MeetingNotFoundException;
import com.studybuddies.server.web.dto.MeetingChangeRequest;
import com.studybuddies.server.web.dto.MeetingCreationRequest;
import com.studybuddies.server.web.dto.MeetingResponse;
import com.studybuddies.server.web.mapper.MeetingMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MeetingService {

  private final MeetingMapper meetingMapper;
  private final MeetingRepository meetingRepository;
  private final UserService userService;

  @Transactional
  public Long saveMeetingToDatabase(MeetingCreationRequest mcr, String uuid) {
    MeetingEntity meetingEntity = meetingMapper.MeetingCreationRequestToMeetingEntity(mcr);
    UserEntity creator = userService.findByUUID(UUIDService.parseUUID(uuid));
    meetingEntity.setCreator(creator);
    return meetingRepository.save(meetingEntity).getId();
  }

  @Transactional
  public void changeMeetingInDatabase(Long id, MeetingChangeRequest meetingChangeRequest) {
    Optional<MeetingEntity> requestResult = meetingRepository.findById(id);

    if (requestResult.isEmpty()) {
      throw new MeetingNotFoundException("");
    }
    MeetingEntity meetingEntity = requestResult.get();
    MeetingEntity changedMeeting = meetingMapper.MeetingChangeRequestToMeetingEntity(
        meetingChangeRequest);

    setIfNotNull(changedMeeting.getTitle(), meetingEntity::setTitle);
    setIfNotNull(changedMeeting.getDescription(), meetingEntity::setDescription);
    setIfNotNull(changedMeeting.getDate_from(), meetingEntity::setDate_from);
    setIfNotNull(changedMeeting.getDate_until(), meetingEntity::setDate_until);
    setIfNotNull(changedMeeting.getPlace(), meetingEntity::setPlace);
    setIfNotNull(changedMeeting.getRepeatable(), meetingEntity::setRepeatable);
    meetingMapper.validate(meetingEntity);
    meetingRepository.save(meetingEntity);
  }

  @Transactional
  public String retrieveMeetingFromDatabase(Long id) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      if (id == null) {
        ArrayList<MeetingResponse> meetings = findAllMeetingEntities();
        return mapper.writeValueAsString(meetings);
      }
      MeetingEntity meeting = meetingRepository.findById(id)
          .orElseThrow(() -> new MeetingNotFoundException(""));
      return mapper.writeValueAsString(meetingMapper.MeetingEntityToMeetingResponse(meeting));
    } catch (JsonProcessingException e) {
      return "Error processing data";
    }
  }

  @Transactional
  public void deleteMeetingFromDatabase(Long id) {
    meetingRepository.deleteById(id);
  }

  @Transactional
  public ArrayList<MeetingResponse> findAllMeetingEntities() {
    ArrayList<MeetingResponse> meetings = new ArrayList<>();
    Iterable<MeetingEntity> meetingIterator = meetingRepository.findAll();

    for (MeetingEntity e : meetingIterator) {
      meetings.add(meetingMapper.MeetingEntityToMeetingResponse(e));
    }
    return meetings;
  }

  private <T> void setIfNotNull(T value, Consumer<T> setFunc) {
    if (value != null) {
      setFunc.accept(value);
    }
  }
}
