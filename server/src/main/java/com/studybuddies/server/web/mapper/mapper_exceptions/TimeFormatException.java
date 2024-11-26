package com.studybuddies.server.web.mapper.mapper_exceptions;

import com.studybuddies.server.services.exceptions.StudyBuddiesException;

public class TimeFormatException extends StudyBuddiesException {

  public TimeFormatException(String message) {
    super(message);
  }
}
