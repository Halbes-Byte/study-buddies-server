package com.studybuddies.server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.domain.UserEntity;
import com.studybuddies.server.persistance.MeetingRepository;
import com.studybuddies.server.services.exceptions.InvalidUUIDException;
import com.studybuddies.server.services.exceptions.MeetingNotFoundException;
import com.studybuddies.server.web.dto.MeetingChangeRequest;
import com.studybuddies.server.web.dto.MeetingCreationRequest;
import com.studybuddies.server.web.dto.MeetingResponse;
import com.studybuddies.server.web.mapper.MeetingMapper;
import com.studybuddies.server.web.mapper.MeetingMapperUtils;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MeetingService {

  private final MeetingMapper meetingMapper;
  private final MeetingRepository meetingRepository;
  private final UserService userService;
  private final MeetingMapperUtils meetingMapperUtils;

  @Transactional
  public Long saveMeetingToDatabase(MeetingCreationRequest mcr, String uuid) {
    MeetingEntity meetingEntity = meetingMapper.meetingCreationRequestToMeetingEntity(mcr);
    UserEntity creator = userService.findByUUID(UUIDService.parseUUID(uuid));
    meetingEntity.setCreator(creator);
    return meetingRepository.save(meetingEntity).getId();
  }

  @Transactional
  public void changeMeetingInDatabase(Long id, MeetingChangeRequest meetingChangeRequest, String uuid) {
    Optional<MeetingEntity> requestResult = meetingRepository.findById(id);

    if (requestResult.isEmpty()) {
      throw new MeetingNotFoundException("");
    }
    MeetingEntity meetingEntity = requestResult.get();

    if (!meetingEntity.getCreator().getUuid().equals(UUIDService.parseUUID(uuid))) {
      throw new MeetingNotFoundException("");
    }

    if(meetingChangeRequest.getCreator() != null
        && !userService.existsByUUID(UUIDService.parseUUID(uuid))
    ) {
        throw new InvalidUUIDException("");
      }

    MeetingEntity changedMeetingEntity = meetingMapper.meetingChangeRequestToMeetingEntity(meetingChangeRequest);
    MergingService.mergeObjects(changedMeetingEntity, meetingEntity);

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
      return mapper.writeValueAsString(meetingMapper.meetingEntityToMeetingResponse(meeting));
    } catch (JsonProcessingException e) {
      return "Error processing data";
    }
  }

  @Transactional
  public void deleteMeetingFromDatabase(Long id, String uuid) {
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
