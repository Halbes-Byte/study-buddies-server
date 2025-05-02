package com.studybuddies.server.services.module;

import com.studybuddies.server.domain.ModuleEntity;
import com.studybuddies.server.persistance.ModuleRepository;
import com.studybuddies.server.services.exceptions.InvalidModuleNameException;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ModuleValidationService {

  private final ModuleRepository moduleRepository;
  private final ModuleUtilService moduleUtilService;

  public boolean exists(String module) {
    if(module.length() < 6 || module.length() > 50) {
      throw new InvalidModuleNameException("");
    }
    String creationModule = moduleUtilService.normalizeModuleName(module);
    Iterable<ModuleEntity> iterable = moduleRepository.findAll();

    if(!iterable.iterator().hasNext()) {
      return false;
    }

    return StreamSupport.stream(iterable.spliterator(), false)
            .map(ModuleEntity::getName)
            .map(moduleUtilService::normalizeModuleName)
            .anyMatch(normalizedName -> normalizedName.equals(creationModule));
  }

}
