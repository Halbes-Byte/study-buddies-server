package com.studybuddies.server.persistance;

import com.studybuddies.server.domain.StudyGroupEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;

@Repository
public interface StudyGroupRepository extends CrudRepository<StudyGroupEntity, StudyGroupId> {
    List<StudyGroupEntity> findByMeeting_Id(UUID meetingId);
    List<StudyGroupEntity> findByUser_Uuid(UUID userId);
    void deleteByMeeting_Id(UUID meetingId);
    void deleteByUser_Uuid(UUID userId);
}

