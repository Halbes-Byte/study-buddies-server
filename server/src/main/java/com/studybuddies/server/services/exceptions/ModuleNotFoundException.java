package com.studybuddies.server.services.exceptions;

public class ModuleNotFoundException extends StudyBuddiesException {
  public ModuleNotFoundException(String m) {
    super(m);
  }
}

