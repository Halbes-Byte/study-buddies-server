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
import com.studybuddies.server.web.dto.StudyGroupResponse;
import com.studybuddies.server.web.mapper.StudyGroupMapper;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudyGroupService implements CRUDService<StudyGroupJoinRequest, StudyGroupLeaveRequest, StudyGroupResponse> {
    private final StudyGroupRepository studyGroupRepository;
    private final MeetingService meetingService;
    private final UserService userService;
    private final StudyGroupMapper studyGroupMapper;

    @Override
    public List<StudyGroupResponse> get(String id) {
        var studyGroupEntities =  findStudyGroupsByUUID(id);
        List<StudyGroupResponse> responses = new ArrayList<>();
        for (StudyGroupEntity studyGroupEntity : studyGroupEntities) {
            responses.add(studyGroupMapper.studyGroupEntityToStudyGroupResponse(studyGroupEntity));
        }
        return responses;
    }

    @Override
    public void create(StudyGroupJoinRequest request, String clientUuid) {

        MeetingEntity meetingEntity = meetingService.findMeetingByUUID(request.meetingId);
        UserEntity userEntity = userService.findByUUID(UUIDService.parseUUID(clientUuid));

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

    private void joinMeeting(UserEntity userEntity, MeetingEntity meetingEntity) {
        var entity = StudyGroupEntity.builder().user(userEntity).meeting(meetingEntity).build();
        studyGroupRepository.save(entity);
    }

    private void leaveMeeting(String userUUID, String meetingUUID) {
        studyGroupRepository.deleteByUserIdAndMeetingId(UUIDService.parseUUID(userUUID), UUIDService.parseUUID(meetingUUID));
    }

    private List<StudyGroupEntity> findStudyGroupsByUUID(String someUUID) {
        return studyGroupRepository.findByUserIdOrMeetingId(UUIDService.parseUUID(someUUID))
                .orElseThrow(() -> new InvalidUUIDException("UUID was invalid"));
    }
}
