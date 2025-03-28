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
  // Mappings for meetingCreationRequestToMeetingEntity
  @Mapping(source = "title", target = "title")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "place", target = "place")
  @Mapping(source = "date_from", target = "date_from", qualifiedByName = "stringToLocalDate")
  @Mapping(source = "date_until", target = "date_until", qualifiedByName = "stringToLocalDate")
  @Mapping(source = "repeatable", target = "repeatable", qualifiedByName = "stringToRepeatEnum")
  MeetingEntity meetingCreationRequestToMeetingEntity(MeetingCreationRequest meetingCreationRequest);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "superId", target = "superId")
  @Mapping(source = "title", target = "title")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "place", target = "place")
  @Mapping(source = "date_from", target = "date_from")
  @Mapping(source = "date_until", target = "date_until")
  @Mapping(source = "repeatable", target = "repeatable")
  @Mapping(source = "creator", target = "creator", qualifiedByName = "userEntityToUUIDString")
  MeetingResponse meetingEntityToMeetingResponse(MeetingEntity meetingEntity);

  // Mappings for meetingChangeRequestToMeetingEntity
  @Mapping(source = "title", target = "title")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "place", target = "place")
  @Mapping(source = "date_from", target = "date_from", qualifiedByName = "changeStringToLocalDate")
  @Mapping(source = "date_until", target = "date_until", qualifiedByName = "changeStringToLocalDate")
  @Mapping(source = "repeatable", target = "repeatable", qualifiedByName = "changeStringToRepeatEnum")
  @Mapping(source = "creator", target = "creator", qualifiedByName = "stringToUserEntity")
  MeetingEntity meetingChangeRequestToMeetingEntity(MeetingChangeRequest meetingChangeRequest);

  @AfterMapping
  default void validate(@MappingTarget MeetingEntity meetingEntity) {
    if(meetingEntity.getDate_from() == null || meetingEntity.getDate_until() == null) {
      return;
    }
    LocalDateTime start = meetingEntity.getDate_from();
    LocalDateTime end = meetingEntity.getDate_until();

    if(start.isAfter(end)) {
      throw new EndDateAfterStartDateException("");
    }
  }
}
