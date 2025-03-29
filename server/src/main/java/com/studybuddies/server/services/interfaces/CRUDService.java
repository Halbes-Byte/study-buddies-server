package com.studybuddies.server.services.interfaces;

import com.studybuddies.server.web.dto.interfaces.ChangeRequest;
import com.studybuddies.server.web.dto.interfaces.CreationRequest;
import jakarta.transaction.Transactional;
import java.util.UUID;

public interface CRUDService<T extends CreationRequest, R extends ChangeRequest> {
  @Transactional
  String get(UUID id);
  @Transactional
  void create(T request, String clientUUID);
  @Transactional
  void update(UUID targetUUID, R request, String clientUUID);
  @Transactional
  void delete(UUID targetUUID, String clientUUID);
}
