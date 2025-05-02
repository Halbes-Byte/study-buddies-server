package com.studybuddies.server.services.meeting;

import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.persistance.MeetingRepository;
import com.studybuddies.server.services.UUIDService;
import com.studybuddies.server.services.user.UserService;
import com.studybuddies.server.services.exceptions.MeetingNotFoundException;
import com.studybuddies.server.services.interfaces.CRUDService;
import com.studybuddies.server.web.dto.meeting.MeetingChangeRequest;
import com.studybuddies.server.web.dto.meeting.MeetingCreationRequest;
import com.studybuddies.server.web.dto.meeting.MeetingResponse;
import com.studybuddies.server.web.mapper.MeetingMapper;
import java.util.ArrayList;
import java.util.List;
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
  private final UserService userService;
  private final MeetingCreationService meetingCreationService;
  private final MeetingChangeService meetingChangeService;

  @Override
  public List<MeetingResponse> get(String meetingId) {
    List<MeetingResponse> responses = new ArrayList<>();

    if (meetingId == null) {
      return findAllMeetingEntities();
    }
    List<MeetingEntity> meetings = meetingRepository.findBySuperId(
        UUIDService.parseUUID(meetingId));

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

  private List<MeetingResponse> findAllMeetingEntities() {
    ArrayList<MeetingResponse> meetings = new ArrayList<>();
    Iterable<MeetingEntity> meetingIterator = meetingRepository.findAll();

    for (MeetingEntity e : meetingIterator) {
      meetings.add(meetingMapper.of(e));
    }
    return meetings;
  }

  public MeetingEntity findMeetingByUUID(String uuid) {
    return meetingRepository.findById(UUIDService.parseUUID(uuid)).orElse(null);
  }
}

