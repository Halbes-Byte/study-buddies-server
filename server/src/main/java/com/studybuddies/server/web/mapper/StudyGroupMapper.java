package com.studybuddies.server.web.mapper;

import com.studybuddies.server.domain.StudyGroupEntity;
import com.studybuddies.server.web.dto.StudyGroupResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StudyGroupMapperUtils.class})
public interface StudyGroupMapper {

  @Mapping(source = "user", target = "userId", qualifiedByName = "userEntityToId")
  @Mapping(source = "meeting", target = "meetingId", qualifiedByName = "meetingEntityToId")
  StudyGroupResponse studyGroupEntityToStudyGroupResponse(StudyGroupEntity studyGroupEntity);

}
