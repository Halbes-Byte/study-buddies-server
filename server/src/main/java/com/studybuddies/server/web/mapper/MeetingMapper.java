package com.studybuddies.server.web.mapper;

import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.web.dto.MeetingChangeRequest;
import com.studybuddies.server.web.dto.MeetingCreationRequest;
import com.studybuddies.server.web.dto.MeetingResponse;
import com.studybuddies.server.web.mapper.exceptions.EndDateAfterStartDateException;
import java.time.LocalDateTime;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {MeetingMapperUtils.class})
public interface MeetingMapper {
  // Mappings for MeetingCreationRequestToMeetingEntity
  @Mapping(source = "title", target = "title")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "place", target = "place")
  @Mapping(source = "date_from", target = "date_from", qualifiedByName = "stringToLocalDate")
  @Mapping(source = "date_until", target = "date_until", qualifiedByName = "stringToLocalDate")
  @Mapping(source = "repeatable", target = "repeatable", qualifiedByName = "stringToRepeatEnum")

  MeetingEntity MeetingCreationRequestToMeetingEntity(MeetingCreationRequest meetingCreationRequest);

  // Mappings for MeetingChangeRequestToMeetingEntity
  @Mapping(source = "id", target = "id")
  @Mapping(source = "title", target = "title")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "place", target = "place")
  @Mapping(source = "date_from", target = "date_from", qualifiedByName = "changeStringToLocalDate")
  @Mapping(source = "date_until", target = "date_until", qualifiedByName = "changeStringToLocalDate")
  @Mapping(source = "repeatable", target = "repeatable", qualifiedByName = "stringToRepeatEnum")
  MeetingEntity MeetingChangeRequestToMeetingEntity(MeetingChangeRequest meetingChangeRequest);
  MeetingResponse MeetingEntityToMeetingResponse(MeetingEntity meetingEntity);

  @AfterMapping
  default void validate(@MappingTarget MeetingEntity meetingEntity) {
    LocalDateTime start = meetingEntity.getDate_from();
    LocalDateTime end = meetingEntity.getDate_until();

    if(start.isAfter(end)) {
      throw new EndDateAfterStartDateException("");
    }
  }
}
