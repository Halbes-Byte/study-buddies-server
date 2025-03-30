package com.studybuddies.server.services.meeting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.persistance.MeetingRepository;
import com.studybuddies.server.services.UUIDService;
import com.studybuddies.server.services.user.UserService;
import com.studybuddies.server.services.exceptions.MeetingNotFoundException;
import com.studybuddies.server.services.interfaces.CRUDService;
import com.studybuddies.server.web.dto.MeetingChangeRequest;
import com.studybuddies.server.web.dto.MeetingCreationRequest;
import com.studybuddies.server.web.dto.MeetingResponse;
import com.studybuddies.server.web.mapper.MeetingMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MeetingService implements CRUDService<MeetingCreationRequest, MeetingChangeRequest> {

  private final MeetingMapper meetingMapper;
  private final MeetingRepository meetingRepository;
  private final UserService userService;
  private final MeetingCreationService meetingCreationService;
  private final MeetingChangeService meetingChangeService;

  @Override
  public String get(UUID meetingId) {
    if(true) throw new NotImplementedException("Not implemented correctly");
    ObjectMapper mapper = new ObjectMapper();
    List<MeetingResponse> responses = new ArrayList<>();
    try {
      if (meetingId == null) {
        List<MeetingResponse> meetings = findAllMeetingEntities();
        return mapper.writeValueAsString(meetings);
      }
      List<MeetingEntity> meetings = meetingRepository.findBySuperId(meetingId);

      for(var meetingEntity : meetings) {
        responses.add(meetingMapper.meetingEntityToMeetingResponse(meetingEntity));
      }
      return mapper.writeValueAsString(responses);
    } catch (JsonProcessingException e) {
      return "Error processing data";
    }
  }

  @Override
  public void create(MeetingCreationRequest mcr, String creatorUuid) {
    meetingCreationService.createMeetingInstances(mcr, creatorUuid, null, false);
  }

  @Override
  public void update(
      UUID meetingId,
      MeetingChangeRequest meetingChangeRequest,
      String clientUUID
  ) {
    meetingChangeService.update(meetingId, meetingChangeRequest, clientUUID);
  }

  @Override
  public void delete(UUID targetUUID, String clientUUID) {
    if(true) throw new NotImplementedException("Not implemented correctly");
    MeetingEntity meeting = meetingRepository.findById(targetUUID)
            .orElseThrow(() -> new MeetingNotFoundException(""));

    if (meeting.getCreator().getUuid().equals(UUIDService.parseUUID(clientUUID))) {
      meetingRepository.deleteById(targetUUID);
    } else {
      throw new MeetingNotFoundException("");
    }
  }

  private List<MeetingResponse> findAllMeetingEntities() {
    ArrayList<MeetingResponse> meetings = new ArrayList<>();
    Iterable<MeetingEntity> meetingIterator = meetingRepository.findAll();

    for (MeetingEntity e : meetingIterator) {
      meetings.add(meetingMapper.meetingEntityToMeetingResponse(e));
    }
    return meetings;
  }

  public MeetingEntity findMeetingByUUID(String uuid) {
    return meetingRepository.findById(UUIDService.parseUUID(uuid)).orElseThrow();
  }
}

