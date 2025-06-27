package com.studybuddies.server.web.mapper;

import com.studybuddies.server.domain.UserModule;
import com.studybuddies.server.web.dto.user.UserModuleReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ChapterMapper.class})
public interface UserModuleMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "chapter", source = "chapter")
  UserModule of(UserModuleReq dto);
}

