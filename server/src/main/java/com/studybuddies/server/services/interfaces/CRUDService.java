package com.studybuddies.server.services.interfaces;

import com.studybuddies.server.domain.Filter;
import com.studybuddies.server.web.dto.interfaces.ChangeRequest;
import com.studybuddies.server.web.dto.interfaces.CreationRequest;
import com.studybuddies.server.web.dto.interfaces.Responses;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CRUDService<T extends CreationRequest, R extends ChangeRequest, S extends Responses> {

  @Transactional
  default List<S> get() {
    throw new UnsupportedOperationException("This operation is not supported");
  }

  @Transactional
  default List<S> get(Filter filter) {
    throw new UnsupportedOperationException("This operation is not supported");
  }

  @Transactional
  default List<S> get(String id, Filter filter) {
    throw new UnsupportedOperationException("This operation is not supported");
  }

  @Transactional
  default List<S> get(String id, String clientUUID, Filter filter) {
    throw new UnsupportedOperationException("This operation is not supported");
  }

  @Transactional
  default void create(T request, String clientUUID) {
    throw new UnsupportedOperationException("This operation is not supported");
  }

  @Transactional
  default void update(String targetUUID, R request, String clientUUID) {
    throw new UnsupportedOperationException("This operation is not supported");
  }

  @Transactional
  default void delete(String targetUUID, String clientUUID) {
    throw new UnsupportedOperationException("This operation is not supported");
  }
}
