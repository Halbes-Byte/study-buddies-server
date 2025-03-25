package com.studybuddies.server.services;

import com.studybuddies.server.services.exceptions.InvalidUUIDException;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UUIDService {
  public static UUID parseUUID(String uuidString) {
    try {
      return UUID.fromString(uuidString);
    } catch (IllegalArgumentException e) {
      throw new InvalidUUIDException("");
    }
  }
}
