package com.studybuddies.server.persistance;

import com.studybuddies.server.domain.MeetingEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepository extends CrudRepository<MeetingEntity, Long> {
    void deleteById(Long id);
}
