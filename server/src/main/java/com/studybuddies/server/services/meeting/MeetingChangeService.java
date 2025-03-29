package com.studybuddies.server.services.meeting;

import com.studybuddies.server.domain.ChangeType;
import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.persistance.MeetingRepository;
import com.studybuddies.server.services.MergingService;
import com.studybuddies.server.services.UUIDService;
import com.studybuddies.server.services.user.UserService;
import com.studybuddies.server.services.exceptions.InvalidUUIDException;
import com.studybuddies.server.services.exceptions.MeetingNotFoundException;
import com.studybuddies.server.web.dto.MeetingChangeRequest;
import com.studybuddies.server.web.mapper.MeetingMapper;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingChangeService {

  private final MeetingRepository meetingRepository;
  private final MeetingMapper meetingMapper;
  private final MeetingCreationService meetingCreationService;
  private final UserService userService;

  public void update(
      UUID meetingId,
      MeetingChangeRequest meetingChangeRequest,
      String clientUUID
  ) {
    List<MeetingEntity> meetingsToChange = getMeetingsThatShouldChange(meetingChangeRequest.getChangeType(), meetingId);

    MeetingEntity baseMeeting = meetingsToChange.get(0);

    if(!userService.existsByUUID(UUIDService.parseUUID(clientUUID))) {
      throw new InvalidUUIDException("");
    }

    if (isNotMeetingOwner(baseMeeting, clientUUID)) {
      throw new MeetingNotFoundException("");
    }

    changeMeetings(meetingsToChange, meetingChangeRequest);
  }

  private void changeMeetings(List<MeetingEntity> meetingEntities, MeetingChangeRequest meetingChangeRequest) {
    MeetingEntity meetingDiff = meetingMapper.meetingChangeRequestToMeetingEntity(meetingChangeRequest);
    MeetingEntity meetingWithChangesApplied = meetingEntities.get(0);
    MergingService.mergeObjects(meetingDiff, meetingWithChangesApplied);

    String creatorUuid = meetingWithChangesApplied.getCreator().getUuid().toString();

    boolean isUpdate = meetingEntities.size() == 1;

    meetingRepository.deleteAll(meetingEntities);
    meetingCreationService.createMeetingInstances(
        meetingMapper.meetingEntityToMeetingCreationRequest(meetingWithChangesApplied),
        creatorUuid,
        meetingWithChangesApplied.getSuperId(),
        isUpdate);
  }

  private List<MeetingEntity> getMeetingsThatShouldChange(ChangeType changeType, UUID meetingId) {
    List<MeetingEntity> requestResult;
    var entity = meetingRepository.findById(meetingId);

    if(entity.isEmpty()) {
      throw new MeetingNotFoundException("");
    }

    if(changeType == ChangeType.OCCURRENCE) {
      requestResult = List.of(entity.get());
    } else {
      requestResult = meetingRepository.findBySuperId(entity.get().getSuperId());
    }

    if(requestResult.isEmpty()) {
      throw new MeetingNotFoundException("");
    }

    return requestResult;
  }

  private boolean isNotMeetingOwner(MeetingEntity meetingEntity, String creatorUuid) {
    return !meetingEntity.getCreator().getUuid().equals(UUIDService.parseUUID(creatorUuid));
  }
}
