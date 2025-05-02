package com.studybuddies.server.web.mapper;

import com.studybuddies.server.domain.Repeat;
import com.studybuddies.server.domain.UserEntity;
import com.studybuddies.server.persistance.ModuleRepository;
import com.studybuddies.server.services.module.ModuleCrudService;
import com.studybuddies.server.services.module.ModuleValidationService;
import com.studybuddies.server.services.exceptions.ModuleNotFoundException;
import com.studybuddies.server.web.mapper.exceptions.DateFormatException;
import com.studybuddies.server.web.mapper.exceptions.InvalidRepeatStringException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.AllArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MeetingMapperUtils {

  private final ModuleValidationService moduleValidationService;
  private final ModuleCrudService moduleCrudService;
  private final ModuleRepository moduleRepository;


  // MeetingCreationRequest
  @Named("stringToLocalDate")
  public LocalDateTime stringToLocalDate(String dateString) {
    return stringToLocalDateTime(dateString);
  }

  // MeetingCreationRequest
  @Named("stringToRepeatEnum")
  public Repeat stringToRepeatEnum(String repeatString) {
    return stringToRepeat(repeatString);
  }

  @Named("localDateTimeToString")
  public String localDateTimeToString(LocalDateTime localDateTime) {
    if (localDateTime == null) {
      return null;
    }
    return localDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy:HH:mm"));
  }

  // MeetingChangeRequest
  @Named("changeStringToLocalDate")
  public LocalDateTime changeStringToLocalDate(String dateString) {
    if (dateString == null || dateString.trim().isEmpty()) {
      return null;
    }
    return stringToLocalDateTime(dateString);
  }

  // MeetingChangeRequest
  @Named("changeStringToRepeatEnum")
  public Repeat changeStringToRepeatEnum(String repeatString) {
    if (repeatString == null || repeatString.trim().isEmpty()) {
      return null;
    }
    return stringToRepeat(repeatString);
  }

  @Named("userEntityToUUIDString")
  public String userEntityToUUIDString(UserEntity userEntity) {
    if (userEntity == null) {
      return null;
    }
    return userEntity.getUuid().toString();
  }

  @Named("assignExistingModule")
  public String assignExistingModule(String module) {
    var foundModule = moduleCrudService.get();

    if(!moduleValidationService.exists(module) || foundModule.isEmpty()) {
      throw new ModuleNotFoundException("");
    }
    return foundModule.get(0).getName().toUpperCase();
  }

  private Repeat stringToRepeat(String repeatString) {
    try {
      return Repeat.valueOf(repeatString.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new InvalidRepeatStringException("");
    }
  }

  private LocalDateTime stringToLocalDateTime(String dateString) {
    if (dateString == null || dateString.trim().isEmpty()) {
      return null;
    }

    // only accept values in following format: dd-MM-yyyy:hh:mm
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy:HH:mm");
    LocalDateTime dueDate;

    try {
      dueDate = LocalDateTime.parse(dateString, format);
    } catch (DateTimeParseException e) {
      throw new DateFormatException("");
    }
    return dueDate;
  }
}
