package com.studybuddies.server.web.repositories;

import com.studybuddies.server.domain.MeetingEntity;
import org.aspectj.apache.bcel.util.Repository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MeetingRepository extends CrudRepository<MeetingEntity, Long> {
    public List<MeetingEntity> findById(long id);
}
