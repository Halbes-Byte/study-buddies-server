package com.studybuddies.server.services.meeting;

import com.studybuddies.server.domain.Filter;
import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.persistance.MeetingRepository;
import com.studybuddies.server.services.UUIDService;
import com.studybuddies.server.services.exceptions.MeetingNotFoundException;
import com.studybuddies.server.services.exceptions.ModuleNotFoundException;
import com.studybuddies.server.services.interfaces.CRUDService;
import com.studybuddies.server.web.dto.meeting.MeetingChangeRequest;
import com.studybuddies.server.web.dto.meeting.MeetingCreationRequest;
import com.studybuddies.server.web.dto.meeting.MeetingResponse;
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
public class MeetingService implements
    CRUDService<MeetingCreationRequest, MeetingChangeRequest, MeetingResponse> {

  private final MeetingMapper meetingMapper;
  private final MeetingRepository meetingRepository;
  private final MeetingCreationService meetingCreationService;
  private final MeetingChangeService meetingChangeService;

  @Override
  public List<MeetingResponse> get(String clientUUID, Filter filter) {
    List<MeetingResponse> responses = new ArrayList<>();
    List<MeetingEntity> meetings;
    UUID client = UUIDService.parseUUID(clientUUID);

    if(filter == null) {
      meetings = meetingRepository.findAll(MeetingSpecificationService.getSpecParticipant(client));
    } else {
      var module = filter.getFilters().get("module");

      if(module != null)
        meetings = meetingRepository.findAll(MeetingSpecificationService.getSpecRelevantMeetings(UUIDService.parseUUID(clientUUID), module.toUpperCase()));
      else
        throw new ModuleNotFoundException("Module not found");
    }

    for (var meetingEntity : meetings) {
      responses.add(meetingMapper.of(meetingEntity));
    }
    return responses;
  }

  @Override
  public void create(MeetingCreationRequest mcr, String creatorUuid) {
    meetingCreationService.createMeetingInstances(mcr, creatorUuid, null, false);
  }

  @Override
  public void update(
      String meetingId,
      MeetingChangeRequest meetingChangeRequest,
      String clientUUID
  ) {
    meetingChangeService.update(UUIDService.parseUUID(meetingId), meetingChangeRequest, clientUUID);
  }

  @Override
  public void delete(String targetUUID, String clientUUID) {
    if (true) {
      throw new NotImplementedException("Not implemented correctly");
    }
    MeetingEntity meeting = meetingRepository.findById(UUIDService.parseUUID(targetUUID))
        .orElseThrow(() -> new MeetingNotFoundException(""));

    if (meeting.getCreator().getUuid().equals(UUIDService.parseUUID(clientUUID))) {
      meetingRepository.deleteById(UUIDService.parseUUID(targetUUID));
    } else {
      throw new MeetingNotFoundException("");
    }
  }

  public MeetingEntity findMeetingByUUID(String uuid) {
    return meetingRepository.findById(UUIDService.parseUUID(uuid)).orElse(null);
  }

  public List<MeetingEntity> findMeetingsBySuperID(String superID) {
    return meetingRepository.findBySuperId(UUIDService.parseUUID(superID));
  }
}

