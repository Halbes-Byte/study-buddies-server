package com.studybuddies.server.web.mapper.exceptions;

import com.studybuddies.server.services.exceptions.StudyBuddiesException;

public class InvalidRepeatStringException extends StudyBuddiesException {

  public InvalidRepeatStringException(String message) {
    super(message);
  }
}
