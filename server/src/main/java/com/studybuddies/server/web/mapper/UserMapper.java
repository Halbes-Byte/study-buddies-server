package com.studybuddies.server.web.mapper;

import com.studybuddies.server.domain.UserEntity;
import com.studybuddies.server.web.dto.UserAccountSetupRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(source = "username", target = "username")
  UserEntity toUserEntity(UserAccountSetupRequest userAccountSetupRequest);
}
