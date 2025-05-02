package com.studybuddies.server.persistance;

import com.studybuddies.server.domain.ModuleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends CrudRepository<ModuleEntity, String> {
}
