package com.studybuddies.server.services.module;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ModuleUtilService {
  protected String normalizeModuleName(String moduleName) {
    return moduleName.toLowerCase().replaceAll("[^a-z0-9]", "");
  }
}
