package com.studybuddies.server.web.mapper.exceptions;

import com.studybuddies.server.services.exceptions.StudyBuddiesException;

public class AccountSetupAlreadyFinished extends StudyBuddiesException {

  public AccountSetupAlreadyFinished(String message) {
    super(message);
  }
}
