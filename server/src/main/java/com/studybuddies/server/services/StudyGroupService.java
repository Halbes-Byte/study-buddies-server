package com.studybuddies.server.services;

import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.domain.StudyGroupEntity;
import com.studybuddies.server.domain.UserEntity;
import com.studybuddies.server.persistance.StudyGroupRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudyGroupService {
    private final StudyGroupRepository studyGroupRepository;

    @Transactional
    public void joinUserToMeeting(UserEntity userEntity, MeetingEntity meetingEntity) {
        studyGroupRepository.save(StudyGroupEntity.builder().user(userEntity).meeting(meetingEntity).build());
    }

    @Transactional
    public void deleteByMeeting(String meetingUUID) {
        studyGroupRepository.deleteByMeeting_Id(UUIDService.parseUUID(meetingUUID));
    }

    @Transactional
    public void deleteByUser(String userUUID) {
        studyGroupRepository.deleteByUser_Uuid(UUIDService.parseUUID(userUUID));
    }

    @Transactional
    public List<MeetingEntity> findByUser(String userUUID) {
        return studyGroupRepository.findByUser_Uuid(UUIDService.parseUUID(userUUID))
                .stream().map(StudyGroupEntity::getMeeting).toList();
    }

    @Transactional
    public List<UserEntity> findByMeeting(String meetingUUID) {
        return studyGroupRepository.findByMeeting_Id(UUIDService.parseUUID(meetingUUID))
                .stream().map(StudyGroupEntity::getUser).toList();
    }


}
