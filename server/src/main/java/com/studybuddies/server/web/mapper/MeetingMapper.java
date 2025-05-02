package com.studybuddies.server.web.mapper;

import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.web.dto.meeting.MeetingChangeRequest;
import com.studybuddies.server.web.dto.meeting.MeetingCreationRequest;
import com.studybuddies.server.web.dto.meeting.MeetingResponse;
import com.studybuddies.server.web.mapper.exceptions.EndDateAfterStartDateException;
import java.time.LocalDateTime;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {MeetingMapperUtils.class})
public interface MeetingMapper {

  @Mapping(source = "title", target = "title", qualifiedByName = "assignExistingModule")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "place", target = "place")
  @Mapping(source = "dateFrom", target = "dateFrom", qualifiedByName = "stringToLocalDate")
  @Mapping(source = "dateUntil", target = "dateUntil", qualifiedByName = "stringToLocalDate")
  @Mapping(source = "repeatable", target = "repeatable", qualifiedByName = "stringToRepeatEnum")
  MeetingEntity of(
      MeetingCreationRequest meetingCreationRequest);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "superId", target = "superId")
  @Mapping(source = "title", target = "title")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "place", target = "place")
  @Mapping(source = "dateFrom", target = "dateFrom")
  @Mapping(source = "dateUntil", target = "dateUntil")
  @Mapping(source = "repeatable", target = "repeatable")
  @Mapping(source = "creator", target = "creator", qualifiedByName = "userEntityToUUIDString")
  MeetingResponse of(MeetingEntity meetingEntity);

  @Mapping(source = "title", target = "title")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "place", target = "place")
  @Mapping(source = "dateFrom", target = "dateFrom", qualifiedByName = "changeStringToLocalDate")
  @Mapping(source = "dateUntil", target = "dateUntil", qualifiedByName = "changeStringToLocalDate")
  @Mapping(source = "repeatable", target = "repeatable", qualifiedByName = "changeStringToRepeatEnum")
  MeetingEntity of(MeetingChangeRequest meetingChangeRequest);

  @Mapping(source = "title", target = "title", qualifiedByName = "assignExistingModule")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "place", target = "place")
  @Mapping(source = "dateFrom", target = "dateFrom", qualifiedByName = "localDateTimeToString")
  @Mapping(source = "dateUntil", target = "dateUntil", qualifiedByName = "localDateTimeToString")
  @Mapping(source = "repeatable", target = "repeatable")
  MeetingCreationRequest meetingEntityToMeetingCreationRequest(MeetingEntity meetingEntity);

  @AfterMapping
  default void validate(@MappingTarget MeetingEntity meetingEntity) {
    if (meetingEntity.getDateFrom() == null || meetingEntity.getDateUntil() == null) {
      return;
    }
    LocalDateTime start = meetingEntity.getDateFrom();
    LocalDateTime end = meetingEntity.getDateUntil();

    if (start.isAfter(end)) {
      throw new EndDateAfterStartDateException("");
    }
  }
}
