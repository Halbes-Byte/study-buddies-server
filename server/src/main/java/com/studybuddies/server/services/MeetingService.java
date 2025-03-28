package com.studybuddies.server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studybuddies.server.domain.ChangeType;
import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.persistance.MeetingRepository;
import com.studybuddies.server.services.exceptions.InvalidUUIDException;
import com.studybuddies.server.services.exceptions.MeetingNotFoundException;
import com.studybuddies.server.web.dto.MeetingChangeRequest;
import com.studybuddies.server.web.dto.MeetingCreationRequest;
import com.studybuddies.server.web.dto.MeetingResponse;
import com.studybuddies.server.web.mapper.MeetingMapper;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MeetingService {

  private final MeetingMapper meetingMapper;
  private final MeetingRepository meetingRepository;
  private final UserService userService;
  private final MeetingCreationService meetingCreationService;

  @Transactional
  public UUID createMeetings(MeetingCreationRequest mcr, String uuid) {
    List<MeetingEntity> meetingEntities = meetingCreationService.createMeetingInstances(mcr, uuid);
    meetingRepository.saveAll(meetingEntities);

    return meetingEntities.get(0).getSuperId();
  }

  @Transactional
  public void changeMeetingInDatabase(UUID id, MeetingChangeRequest meetingChangeRequest, String uuid, ChangeType changeType) {
   if(true) throw new NotImplementedException("Not implemented correctly");

    List<Optional<MeetingEntity>> requestResult;

    if(changeType == ChangeType.OCCURRENCE) {
      requestResult = List.of(meetingRepository.findById(id));
    } else {
      requestResult = meetingRepository.findBySuperId(id);
    }

    if (requestResult.isEmpty()) {
      throw new MeetingNotFoundException("");
    }

    MeetingEntity meetingEntity = requestResult.get(0).get();

    if (!meetingEntity.getCreator().getUuid().equals(UUIDService.parseUUID(uuid))) {
      throw new MeetingNotFoundException("");
    }

    if(meetingChangeRequest.getCreator() != null
        && !userService.existsByUUID(UUIDService.parseUUID(uuid))
    ) {
        throw new InvalidUUIDException("");
      }

    MeetingEntity changedMeetingEntity = meetingMapper.meetingChangeRequestToMeetingEntity(meetingChangeRequest);

    for(var targetMeeting : requestResult) {
      MergingService.mergeObjects(changedMeetingEntity, targetMeeting.get());
    }

    meetingMapper.validate(meetingEntity);
    meetingRepository.save(meetingEntity);
  }

  @Transactional
  public String retrieveMeetingFromDatabase(UUID superId) {
    ObjectMapper mapper = new ObjectMapper();
    List<MeetingResponse> responses = new ArrayList<>();
    try {
      if (superId == null) {
        ArrayList<MeetingResponse> meetings = findAllMeetingEntities();
        return mapper.writeValueAsString(meetings);
      }
      List<Optional<MeetingEntity>> meetings = meetingRepository.findBySuperId(superId);

      for(Optional<MeetingEntity> meetingEntity : meetings) {
        responses.add(meetingMapper.meetingEntityToMeetingResponse(meetingEntity.get()));
      }
      return mapper.writeValueAsString(responses);
    } catch (JsonProcessingException e) {
      return "Error processing data";
    }
  }

  @Transactional
  public void deleteMeetingFromDatabase(UUID id, String uuid) {
    if(true) throw new NotImplementedException("Not implemented correctly");
    MeetingEntity meeting = meetingRepository.findById(id)
            .orElseThrow(() -> new MeetingNotFoundException(""));

    if (meeting.getCreator().getUuid().equals(UUIDService.parseUUID(uuid))) {
      meetingRepository.deleteById(id);
    } else {
      throw new MeetingNotFoundException("");
    }
  }

  public ArrayList<MeetingResponse> findAllMeetingEntities() {
    ArrayList<MeetingResponse> meetings = new ArrayList<>();
    Iterable<MeetingEntity> meetingIterator = meetingRepository.findAll();

    for (MeetingEntity e : meetingIterator) {
      meetings.add(meetingMapper.meetingEntityToMeetingResponse(e));
    }
    return meetings;
  }
}
