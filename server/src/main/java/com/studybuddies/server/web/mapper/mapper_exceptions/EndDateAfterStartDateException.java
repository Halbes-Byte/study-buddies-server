package com.studybuddies.server.web.mapper.mapper_exceptions;

import com.studybuddies.server.services.exceptions.StudyBuddiesException;

public class EndDateAfterStartDateException extends StudyBuddiesException {

  public EndDateAfterStartDateException(String message) {
    super(message);
  }
}
