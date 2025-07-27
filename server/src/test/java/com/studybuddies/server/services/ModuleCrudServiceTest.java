package com.studybuddies.server.services;
import com.studybuddies.server.domain.Filter;
import com.studybuddies.server.domain.ModuleEntity;
import com.studybuddies.server.persistance.ModuleRepository;
import com.studybuddies.server.services.exceptions.ModuleMayAlreadyExistException;
import com.studybuddies.server.services.module.ModuleCrudService;
import com.studybuddies.server.services.module.ModuleUtilService;
import com.studybuddies.server.services.module.ModuleValidationService;
import com.studybuddies.server.web.dto.module.ModuleCreationRequest;
import com.studybuddies.server.web.dto.module.ModuleResponse;
import com.studybuddies.server.web.mapper.ModuleMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModuleCrudServiceTest {

  @Mock
  private ModuleRepository moduleRepository;

  @Mock
  private ModuleMapper moduleMapper;

  @Mock
  private ModuleValidationService moduleValidationService;

  @Mock
  private ModuleUtilService moduleUtilService;

  @InjectMocks
  private ModuleCrudService moduleCrudService;

  @Test
  void get_shouldReturnAllModulesAsMappedResponses() {
    // given
    ModuleEntity module1 = new ModuleEntity();
    module1.setName("MATH101");
    ModuleEntity module2 = new ModuleEntity();
    module2.setName("PHYS201");

    ModuleResponse response1 = new ModuleResponse("MATH101", "2024-01-15", "10:00", "Room A", null);
    ModuleResponse response2 = new ModuleResponse("PHYS201", "2024-01-20", "14:00", "Room B", null);

    List<ModuleEntity> moduleEntities = Arrays.asList(module1, module2);

    when(moduleRepository.findAll()).thenReturn(moduleEntities);
    when(moduleMapper.of(module1)).thenReturn(response1);
    when(moduleMapper.of(module2)).thenReturn(response2);

    // when
    List<ModuleResponse> result = moduleCrudService.get();

    // then
    assertEquals(2, result.size());
    assertEquals("MATH101", result.get(0).getName());
    assertEquals("PHYS201", result.get(1).getName());
    verify(moduleRepository).findAll();
    verify(moduleMapper).of(module1);
    verify(moduleMapper).of(module2);
  }

  @Test
  void get_shouldReturnEmptyListWhenNoModulesExist() {
    // given
    when(moduleRepository.findAll()).thenReturn(Arrays.asList());

    // when
    List<ModuleResponse> result = moduleCrudService.get();

    // then
    assertTrue(result.isEmpty());
    verify(moduleRepository).findAll();
    verifyNoInteractions(moduleMapper);
  }

  @Test
  void getWithFilter_shouldReturnFilteredModulesByName() {
    // given
    String searchName = "math101";
    String normalizedSearchName = "MATH101";
    Filter filter = Filter.of(null, "name", "test");

    ModuleEntity module1 = new ModuleEntity();
    module1.setName("MATH101");
    ModuleEntity module2 = new ModuleEntity();
    module2.setName("PHYS201");
    ModuleEntity module3 = new ModuleEntity();
    module3.setName("MATH101_ADVANCED");

    ModuleResponse response1 = new ModuleResponse("MATH101", "2024-01-15", "10:00", "Room A", null);

    List<ModuleEntity> allModules = Arrays.asList(module1, module2, module3);

    when(moduleUtilService.normalizeModuleName(searchName)).thenReturn(normalizedSearchName);
    when(moduleRepository.findAll()).thenReturn(allModules);
    when(moduleUtilService.normalizeModuleName("MATH101")).thenReturn("MATH101");
    when(moduleUtilService.normalizeModuleName("PHYS201")).thenReturn("PHYS201");
    when(moduleUtilService.normalizeModuleName("MATH101_ADVANCED")).thenReturn("MATH101_ADVANCED");
    when(moduleMapper.of(module1)).thenReturn(response1);

    // when
    List<ModuleResponse> result = moduleCrudService.get(searchName, filter);

    // then
    assertEquals(1, result.size());
    assertEquals("MATH101", result.get(0).getName());
    verify(moduleUtilService).normalizeModuleName(searchName);
    verify(moduleRepository).findAll();
    verify(moduleUtilService).normalizeModuleName("MATH101");
    verify(moduleUtilService).normalizeModuleName("PHYS201");
    verify(moduleUtilService).normalizeModuleName("MATH101_ADVANCED");
    verify(moduleMapper).of(module1);
    verify(moduleMapper, never()).of(module2);
    verify(moduleMapper, never()).of(module3);
  }

  @Test
  void getWithFilter_shouldReturnEmptyListWhenNoMatchingModules() {
    // given
    String searchName = "nonexistent";
    String normalizedSearchName = "NONEXISTENT";
    Filter filter = Filter.of(null, "name", "test");

    ModuleEntity module1 = new ModuleEntity();
    module1.setName("MATH101");

    List<ModuleEntity> allModules = Arrays.asList(module1);

    when(moduleUtilService.normalizeModuleName(searchName)).thenReturn(normalizedSearchName);
    when(moduleRepository.findAll()).thenReturn(allModules);
    when(moduleUtilService.normalizeModuleName("MATH101")).thenReturn("MATH101");

    // when
    List<ModuleResponse> result = moduleCrudService.get(searchName, filter);

    // then
    assertTrue(result.isEmpty());
    verify(moduleUtilService).normalizeModuleName(searchName);
    verify(moduleRepository).findAll();
    verify(moduleUtilService).normalizeModuleName("MATH101");
    verifyNoInteractions(moduleMapper);
  }

  @Test
  void create_shouldCreateModuleSuccessfully() {
    // given
    String clientUUID = UUID.randomUUID().toString();
    ModuleCreationRequest request = new ModuleCreationRequest();
    request.setName("math101");

    ModuleEntity moduleEntity = new ModuleEntity();
    moduleEntity.setName("math101");

    when(moduleValidationService.exists(request.getName())).thenReturn(false);
    when(moduleMapper.of(request)).thenReturn(moduleEntity);

    // when
    moduleCrudService.create(request, clientUUID);

    // then
    assertEquals("MATH101", moduleEntity.getName());
    verify(moduleValidationService).exists(request.getName());
    verify(moduleMapper).of(request);
    verify(moduleRepository).save(moduleEntity);
  }

  @Test
  void create_shouldThrowExceptionWhenModuleAlreadyExists() {
    // given
    String clientUUID = UUID.randomUUID().toString();
    ModuleCreationRequest request = new ModuleCreationRequest();
    request.setName("MATH101");

    when(moduleValidationService.exists(request.getName())).thenReturn(true);

    // when & then
    assertThrows(ModuleMayAlreadyExistException.class, () -> {
      moduleCrudService.create(request, clientUUID);
    });

    verify(moduleValidationService).exists(request.getName());
    verifyNoInteractions(moduleMapper);
    verifyNoInteractions(moduleRepository);
  }

  @Test
  void create_shouldConvertModuleNameToUppercase() {
    // given
    String clientUUID = UUID.randomUUID().toString();
    ModuleCreationRequest request = new ModuleCreationRequest();
    request.setName("math101");

    ModuleEntity moduleEntity = new ModuleEntity();
    moduleEntity.setName("math101");

    when(moduleValidationService.exists(request.getName())).thenReturn(false);
    when(moduleMapper.of(request)).thenReturn(moduleEntity);

    // when
    moduleCrudService.create(request, clientUUID);

    // then
    assertEquals("MATH101", moduleEntity.getName());
    verify(moduleRepository).save(moduleEntity);
  }
}
