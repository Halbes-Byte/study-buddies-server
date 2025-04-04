package com.studybuddies.server.services;

import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.domain.StudyGroupEntity;
import com.studybuddies.server.domain.UserEntity;
import com.studybuddies.server.persistance.StudyGroupRepository;
import com.studybuddies.server.services.exceptions.InvalidUUIDException;
import com.studybuddies.server.services.interfaces.CRUDService;
import com.studybuddies.server.services.meeting.MeetingService;
import com.studybuddies.server.services.user.UserService;
import com.studybuddies.server.web.dto.StudyGroupJoinRequest;
import com.studybuddies.server.web.dto.StudyGroupLeaveRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudyGroupService implements CRUDService<StudyGroupJoinRequest, StudyGroupLeaveRequest, StudyGroupEntity> {
    private final StudyGroupRepository studyGroupRepository;
    private final MeetingService meetingService;
    private final UserService userService;

    @Override
    public List<StudyGroupEntity> get(String id) {
        return findStudyGroupsByUUID(id);
    }

    @Override
    public void create(StudyGroupJoinRequest request, String uuid) {

        MeetingEntity meetingEntity = meetingService.findMeetingByUUID(request.meetingId);
        UserEntity userEntity = userService.findByUUID(UUIDService.parseUUID(uuid));

        joinMeeting(userEntity, meetingEntity);
    }

    @Override
    public void delete(String targetUUID, String clientUUID) {
        leaveMeeting(clientUUID, targetUUID);
    }

    @Override
    public void update(String targetUUID, StudyGroupLeaveRequest request, String clientUUID) {
        // unimplemented
        CRUDService.super.update(targetUUID, request, clientUUID);
    }

    @Transactional
    private void joinMeeting(UserEntity userEntity, MeetingEntity meetingEntity) {
        studyGroupRepository.save(StudyGroupEntity.builder().user(userEntity).meeting(meetingEntity).build());
    }

    @Transactional
    private void leaveMeeting(String userUUID, String meetingUUID) {
        studyGroupRepository.deleteByUserIdAndMeetingId(UUIDService.parseUUID(userUUID), UUIDService.parseUUID(meetingUUID));
    }

    @Transactional
    private List<StudyGroupEntity> findStudyGroupsByUUID(String someUUID) {
        return studyGroupRepository.findByUserIdOrMeetingId(UUIDService.parseUUID(someUUID))
                .orElseThrow(() -> new InvalidUUIDException("UUID was invalid"));
    }
}
