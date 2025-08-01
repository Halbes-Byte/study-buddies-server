package com.studybuddies.server.web.mapper;

import com.studybuddies.server.domain.ChapterEntity;
import com.studybuddies.server.web.dto.chapter.ChapterCreationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CheckboxMapper.class})
public interface ChapterMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "title", source = "title")
  @Mapping(target = "checkbox", source = "checkbox")
  ChapterEntity of(ChapterCreationRequest dto);
}

