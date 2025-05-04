package com.studybuddies.server.web.mapper;

import com.studybuddies.server.domain.UserEntity;
import com.studybuddies.server.web.dto.user.UserAccountSetupRequest;
import com.studybuddies.server.web.dto.user.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(source = "username", target = "username")
  UserEntity of(UserAccountSetupRequest userAccountSetupRequest);

  @Mapping(source = "uuid", target = "uuid")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "name", target = "name")
  UserResponse of(UserEntity userEntity);
}
