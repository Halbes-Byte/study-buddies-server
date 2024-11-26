package com.studybuddies.server.web.mapper;

import com.studybuddies.server.domain.Repeat;
import com.studybuddies.server.web.mapper.mapper_exceptions.DateFormatException;
import com.studybuddies.server.web.mapper.mapper_exceptions.InvalidRepeatStringException;
import com.studybuddies.server.web.mapper.mapper_exceptions.TimeFormatException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class MeetingMapperUtils {
  @Named("stringToLocalDate")
  public LocalDateTime stringToLocalDate(String dateString) {
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

  @Named("stringToRepeatEnum")
  public Repeat stringToRepeatEnum(String repeatString) {
    try {
      return Repeat.valueOf(repeatString.toUpperCase());
    } catch(IllegalArgumentException e) {
      throw new InvalidRepeatStringException("");
    }
  }
}
