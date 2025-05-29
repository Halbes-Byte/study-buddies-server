package com.studybuddies.server.persistance;

import com.studybuddies.server.domain.StudyGroupEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudyGroupRepository extends CrudRepository<StudyGroupEntity, StudyGroupId> {

  @Query("SELECT s FROM StudyGroupEntity s WHERE s.id.userId = :uuid OR s.id.meetingId = :uuid")
  Optional<List<StudyGroupEntity>> findByUserIdOrMeetingId(@Param("uuid") UUID uuid);

  @Modifying
  @Transactional
  @Query("DELETE FROM StudyGroupEntity s WHERE s.id.userId = :userId AND s.id.meetingId = :meetingId")
  void deleteByUserIdAndMeetingId(@Param("userId") UUID userId, @Param("meetingId") UUID meetingId);

  @Modifying
  @Transactional
  @Query("DELETE FROM StudyGroupEntity s WHERE s.user.id = :userId AND s.meeting.superId = :meetingSuperId")
  void deleteByUserIdAndSuperMeetingId(@Param("userId") UUID userId, @Param("meetingSuperId") UUID meetingSuperId);

}


