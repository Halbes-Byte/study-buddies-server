package com.studybuddies.server.persistance;

import com.studybuddies.server.domain.CheckboxEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckboxRepository extends CrudRepository<CheckboxEntity, UUID> {
  List<CheckboxEntity> findByUserUuid(UUID userUuid);
}
