package com.studybuddies.server.web.mapper;

import com.studybuddies.server.domain.CheckboxEntity;
import com.studybuddies.server.web.dto.chapter.CheckboxCreationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CheckboxMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "checked", source = "checked")
  @Mapping(target = "userUuid", ignore = true)
  CheckboxEntity of(CheckboxCreationRequest dto);
}
