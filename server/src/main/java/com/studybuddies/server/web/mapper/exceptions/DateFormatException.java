package com.studybuddies.server.web.mapper.exceptions;

import com.studybuddies.server.services.exceptions.StudyBuddiesException;

public class DateFormatException extends StudyBuddiesException {

  public DateFormatException(String message) {
    super(message);
  }
}
