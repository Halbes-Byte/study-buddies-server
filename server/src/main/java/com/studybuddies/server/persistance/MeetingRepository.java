package com.studybuddies.server.persistance;

import com.studybuddies.server.domain.MeetingEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MeetingRepository extends CrudRepository<MeetingEntity, Long> {
    public List<MeetingEntity> findById(long id);
}
