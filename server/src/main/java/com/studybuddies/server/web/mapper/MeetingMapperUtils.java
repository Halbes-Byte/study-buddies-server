package com.studybuddies.server.web.mapper;

import com.studybuddies.server.domain.Repeat;
import com.studybuddies.server.web.mapper.exceptions.DateFormatException;
import com.studybuddies.server.web.mapper.exceptions.InvalidRepeatStringException;
import com.studybuddies.server.web.mapper.exceptions.TimeFormatException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class MeetingMapperUtils {
  // MeetingCreationRequest
  @Named("stringToLocalDate")
  public LocalDateTime stringToLocalDate(String dateString) {
    return stringToLocalDateTime(dateString);
  }

  // MeetingChangeRequest
  @Named("changeStringToLocalDate")
  public LocalDateTime changeStringToLocalDate(String dateString) {
    if (dateString == null || dateString.trim().isEmpty()) {
      return null;
    }
    return stringToLocalDateTime(dateString);
  }

  // MeetingCreationRequest
  @Named("stringToRepeatEnum")
  public Repeat stringToRepeatEnum(String repeatString) {
    return stringToRepeat(repeatString);
  }

  // MeetingChangeRequest
  @Named("changeStringToRepeatEnum")
  public Repeat changeStringToRepeatEnum(String repeatString) {
    if (repeatString == null || repeatString.trim().isEmpty()) {
      return null;
    }
    return stringToRepeat(repeatString);

  }

  private Repeat stringToRepeat(String repeatString) {
    try {
      return Repeat.valueOf(repeatString.toUpperCase());
    } catch(IllegalArgumentException e) {
      throw new InvalidRepeatStringException("");
    }
  }

  private LocalDateTime stringToLocalDateTime(String dateString) {
    // only accept values in following format: dd-MM-yyyy:hh:mm while mm is divisible by 15
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy:HH:mm");
    LocalDateTime dueDate;

    try {
      dueDate = LocalDateTime.parse(dateString, format);
      if(dueDate.getMinute() % 15 != 0) {
        throw new TimeFormatException("");
      }
    } catch(DateTimeParseException e) {
      throw new DateFormatException("");
    }
    return dueDate;
  }
}
