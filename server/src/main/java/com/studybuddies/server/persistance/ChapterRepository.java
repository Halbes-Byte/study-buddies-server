package com.studybuddies.server.persistance;

import com.studybuddies.server.domain.ChapterEntity;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends CrudRepository<ChapterEntity, UUID> {
}
