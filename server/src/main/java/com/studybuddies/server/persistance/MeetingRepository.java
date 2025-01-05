package com.studybuddies.server.persistance;

import com.studybuddies.server.domain.MeetingEntity;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepository extends CrudRepository<MeetingEntity, Long> {
    Optional<MeetingEntity> findById(Long id);
    void deleteById(Long id);
}
