package com.studybuddies.server.persistance;

import com.studybuddies.server.domain.MeetingEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepository extends CrudRepository<MeetingEntity, UUID> {

  void deleteById(UUID id);

  List<MeetingEntity> findBySuperId(UUID superId);

  Optional<MeetingEntity> findById(UUID id);
}
