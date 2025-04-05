package com.studybuddies.server.persistance;

import com.studybuddies.server.domain.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, UUID> {

  Optional<UserEntity> findById(UUID id);

  boolean existsById(UUID id);

  boolean existsByUsername(String username);

  void deleteById(UUID id);
}
