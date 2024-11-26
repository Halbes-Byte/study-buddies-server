package com.studybuddies.server.web.mapper.mapper_exceptions;

import com.studybuddies.server.services.exceptions.StudyBuddiesException;

public class InvalidRepeatStringException extends StudyBuddiesException {

  public InvalidRepeatStringException(String message) {
    super(message);
  }
}
