package com.studybuddies.server.services;

import com.studybuddies.server.domain.Filter;
import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.domain.StudyGroupEntity;
import com.studybuddies.server.domain.UserEntity;
import com.studybuddies.server.persistance.StudyGroupId;
import com.studybuddies.server.persistance.StudyGroupRepository;
import com.studybuddies.server.services.exceptions.InvalidUUIDException;
import com.studybuddies.server.services.interfaces.CRUDService;
import com.studybuddies.server.services.meeting.MeetingService;
import com.studybuddies.server.services.user.UserService;
import com.studybuddies.server.web.dto.studygroup.StudyGroupJoinRequest;
import com.studybuddies.server.web.dto.studygroup.StudyGroupLeaveRequest;
import com.studybuddies.server.web.dto.studygroup.StudyGroupResponse;
import com.studybuddies.server.web.mapper.StudyGroupMapper;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudyGroupService implements
    CRUDService<StudyGroupJoinRequest, StudyGroupLeaveRequest, StudyGroupResponse> {

  private final StudyGroupRepository studyGroupRepository;
  private final MeetingService meetingService;
  private final UserService userService;
  private final StudyGroupMapper studyGroupMapper;

  @Override
  public List<StudyGroupResponse> get(String id, Filter filter) {
    var studyGroupEntities = findStudyGroupsByUUID(id);
    List<StudyGroupResponse> responses = new ArrayList<>();
    for (StudyGroupEntity studyGroupEntity : studyGroupEntities) {
      responses.add(studyGroupMapper.of(studyGroupEntity));
    }
    return responses;
  }

  @Override
  public void create(StudyGroupJoinRequest request, String clientUuid) {
    UserEntity userEntity = userService.findByUUID(UUIDService.parseUUID(clientUuid));

    if (request.meetingId != null && !request.meetingId.isEmpty()) {
      MeetingEntity meetingEntity = meetingService.findMeetingByUUID(request.meetingId);
      joinMeeting(userEntity, meetingEntity);
    } else if (request.superMeetingId != null && !request.superMeetingId.isEmpty()) {
      List<MeetingEntity> meetingList = meetingService.findMeetingsBySuperID(request.superMeetingId);

      for (MeetingEntity m : meetingList) {
        joinMeeting(userEntity, m);
      }
    }
  }

  @Override
  public void delete(String targetUUID, String clientUUID) {
    leaveMeeting(clientUUID, targetUUID);
    leaveSuperMeeting(clientUUID, targetUUID);
  }

  private void joinMeeting(UserEntity userEntity, MeetingEntity meetingEntity) {
    var id = new StudyGroupId(userEntity.getUuid(), meetingEntity.getId());

    var studyGroup = new StudyGroupEntity();
    studyGroup.setId(id);
    studyGroup.setUser(userEntity);
    studyGroup.setMeeting(meetingEntity);

    studyGroupRepository.save(studyGroup);
  }

  private void leaveMeeting(String userUUID, String meetingUUID) {
    studyGroupRepository.deleteByUserIdAndMeetingId(UUIDService.parseUUID(userUUID),
        UUIDService.parseUUID(meetingUUID));
  }

  private void leaveSuperMeeting(String userUUID, String meetingSuperUUID) {
    studyGroupRepository.deleteByUserIdAndSuperMeetingId(UUIDService.parseUUID(userUUID),
            UUIDService.parseUUID(meetingSuperUUID));
  }

  private List<StudyGroupEntity> findStudyGroupsByUUID(String someUUID) {
    return studyGroupRepository.findByUserIdOrMeetingId(UUIDService.parseUUID(someUUID))
        .orElseThrow(() -> new InvalidUUIDException("UUID was invalid"));
  }
}
