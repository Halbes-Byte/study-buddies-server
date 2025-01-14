package com.studybuddies.server.web.mapper.exceptions;

import com.studybuddies.server.services.exceptions.StudyBuddiesException;

public class EndDateAfterStartDateException extends StudyBuddiesException {

  public EndDateAfterStartDateException(String message) {
    super(message);
  }
}
