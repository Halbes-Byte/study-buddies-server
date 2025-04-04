package com.studybuddies.server.web.mapper;

import com.studybuddies.server.domain.UserEntity;
import com.studybuddies.server.web.dto.UserAccountSetupRequest;
import com.studybuddies.server.web.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(source = "username", target = "username")
  UserEntity toUserEntity(UserAccountSetupRequest userAccountSetupRequest);

  @Mapping(source = "uuid", target = "uuid")
  @Mapping(source = "username", target = "username")
  UserResponse toUserResponse(UserEntity userEntity);
}
