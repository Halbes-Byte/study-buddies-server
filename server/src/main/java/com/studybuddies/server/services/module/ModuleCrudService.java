package com.studybuddies.server.services.module;

import com.studybuddies.server.domain.Filter;
import com.studybuddies.server.domain.ModuleEntity;
import com.studybuddies.server.persistance.ModuleRepository;
import com.studybuddies.server.services.exceptions.ModuleMayAlreadyExistException;
import com.studybuddies.server.services.interfaces.CRUDService;
import com.studybuddies.server.web.dto.module.ModuleChangeRequest;
import com.studybuddies.server.web.dto.module.ModuleCreationRequest;
import com.studybuddies.server.web.dto.module.ModuleResponse;
import com.studybuddies.server.web.mapper.ModuleMapper;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ModuleCrudService implements CRUDService<ModuleCreationRequest, ModuleChangeRequest, ModuleResponse> {

  private final ModuleRepository moduleRepository;
  private final ModuleMapper moduleMapper;
  private final ModuleValidationService moduleValidationService;
  private final ModuleUtilService moduleUtilService;

  @Override
  public List<ModuleResponse> get() {
    return StreamSupport.stream(moduleRepository.findAll().spliterator(), false)
        .map(moduleMapper::of)
        .toList();
  }

  @Override
  public List<ModuleResponse> get(String name, Filter filter) {
    String creationModule = moduleUtilService.normalizeModuleName(name);
    Iterable<ModuleEntity> iterable = moduleRepository.findAll();

    return StreamSupport.stream(iterable.spliterator(), false)
        .filter(moduleEntity -> {
          var normName = moduleUtilService.normalizeModuleName(moduleEntity.getName());
          return normName.equals(creationModule);
        })
        .map(moduleMapper::of)
        .toList();
  }

  @Override
  public void create(ModuleCreationRequest request, String clientUUID) {
    if(moduleValidationService.exists(request.getName())) {
      throw new ModuleMayAlreadyExistException("");
    }
    var moduleEntity = moduleMapper.of(request);
    moduleEntity.setName(moduleEntity.getName().toUpperCase());
    moduleRepository.save(moduleEntity);
  }
}
