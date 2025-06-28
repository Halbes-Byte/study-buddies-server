package com.studybuddies.server.web.mapper;

import com.studybuddies.server.domain.ModuleEntity;
import com.studybuddies.server.web.dto.module.ModuleCreationRequest;
import com.studybuddies.server.web.dto.module.ModuleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ModuleMapper {

  @Mapping(source = "name", target = "name")
  @Mapping(target = "examDate", ignore = true)
  @Mapping(target = "examTime", ignore = true)
  @Mapping(target = "examLoc", ignore = true)
  @Mapping(target = "chapter", ignore = true)
  ModuleResponse of(ModuleEntity moduleEntity);

  @Mapping(source = "name", target = "name")
  ModuleEntity of(ModuleCreationRequest request);
}
