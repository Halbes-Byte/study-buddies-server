package com.studybuddies.server.web.mapper;

import com.studybuddies.server.domain.CheckboxEntity;
import com.studybuddies.server.web.dto.chapter.CheckboxChangeRequest;
import com.studybuddies.server.web.dto.chapter.CheckboxCreationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CheckboxMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "checked", constant = "false")
  @Mapping(target = "chapter", ignore = true)
  CheckboxEntity of(CheckboxCreationRequest dto);

  @Mapping(target = "title", source = "dto.title")
  @Mapping(target = "checked", source = "dto.checked")
  @Mapping(target = "chapter", ignore = true)
  CheckboxEntity of(CheckboxChangeRequest dto);
}
