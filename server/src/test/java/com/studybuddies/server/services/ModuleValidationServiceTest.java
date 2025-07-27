package com.studybuddies.server.services;

import com.studybuddies.server.domain.ModuleEntity;
import com.studybuddies.server.persistance.ModuleRepository;
import com.studybuddies.server.services.exceptions.InvalidModuleNameException;
import com.studybuddies.server.services.module.ModuleUtilService;
import com.studybuddies.server.services.module.ModuleValidationService;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ModuleValidationServiceTest {

  @Mock
  private ModuleRepository moduleRepository;

  @Mock
  private ModuleUtilService moduleUtilService;

  @InjectMocks
  private ModuleValidationService moduleValidationService;

  @Test
  void exists_shouldReturnTrueWhenModuleExists() {
    // given
    String moduleInput = "math101";
    String normalizedInput = "MATH101";

    ModuleEntity module1 = new ModuleEntity();
    module1.setName("MATH101");
    ModuleEntity module2 = new ModuleEntity();
    module2.setName("PHYS201");

    Iterable<ModuleEntity> modules = Arrays.asList(module1, module2);

    when(moduleUtilService.normalizeModuleName(moduleInput)).thenReturn(normalizedInput);
    when(moduleRepository.findAll()).thenReturn(modules);
    when(moduleUtilService.normalizeModuleName("MATH101")).thenReturn("MATH101");

    // when
    boolean result = moduleValidationService.exists(moduleInput);

    // then
    assertTrue(result);
    verify(moduleUtilService).normalizeModuleName(moduleInput);
    verify(moduleRepository).findAll();
    verify(moduleUtilService).normalizeModuleName("MATH101");
  }

  @Test
  void exists_shouldReturnFalseWhenModuleDoesNotExist() {
    // given
    String moduleInput = "chemistry101";
    String normalizedInput = "CHEMISTRY101";

    ModuleEntity module1 = new ModuleEntity();
    module1.setName("MATH101");
    ModuleEntity module2 = new ModuleEntity();
    module2.setName("PHYS201");

    Iterable<ModuleEntity> modules = Arrays.asList(module1, module2);

    when(moduleUtilService.normalizeModuleName(moduleInput)).thenReturn(normalizedInput);
    when(moduleRepository.findAll()).thenReturn(modules);
    when(moduleUtilService.normalizeModuleName("MATH101")).thenReturn("MATH101");
    when(moduleUtilService.normalizeModuleName("PHYS201")).thenReturn("PHYS201");

    // when
    boolean result = moduleValidationService.exists(moduleInput);

    // then
    assertFalse(result);
    verify(moduleUtilService).normalizeModuleName(moduleInput);
    verify(moduleRepository).findAll();
    verify(moduleUtilService).normalizeModuleName("MATH101");
    verify(moduleUtilService).normalizeModuleName("PHYS201");
  }

  @Test
  void exists_shouldReturnFalseWhenNoModulesExist() {
    // given
    String moduleInput = "math101";
    String normalizedInput = "MATH101";
    Iterable<ModuleEntity> emptyModules = Collections.emptyList();

    when(moduleUtilService.normalizeModuleName(moduleInput)).thenReturn(normalizedInput);
    when(moduleRepository.findAll()).thenReturn(emptyModules);

    // when
    boolean result = moduleValidationService.exists(moduleInput);

    // then
    assertFalse(result);
    verify(moduleUtilService).normalizeModuleName(moduleInput);
    verify(moduleRepository).findAll();
  }

  @Test
  void exists_shouldThrowExceptionWhenModuleNameTooShort() {
    // given
    String shortModuleName = "ABC12"; // 5 characters

    // when & then
    assertThrows(InvalidModuleNameException.class, () -> {
      moduleValidationService.exists(shortModuleName);
    });

    verifyNoInteractions(moduleRepository);
    verifyNoInteractions(moduleUtilService);
  }

  @Test
  void exists_shouldThrowExceptionWhenModuleNameTooLong() {
    // given
    String longModuleName = "A".repeat(51); // 51 characters

    // when & then
    assertThrows(InvalidModuleNameException.class, () -> {
      moduleValidationService.exists(longModuleName);
    });

    verifyNoInteractions(moduleRepository);
    verifyNoInteractions(moduleUtilService);
  }

  @Test
  void exists_shouldAcceptValidModuleNameLength() {
    // given
    String validMinLength = "ABCDEF"; // 6 characters
    String normalizedInput = "ABCDEF";
    Iterable<ModuleEntity> emptyModules = Collections.emptyList();

    when(moduleUtilService.normalizeModuleName(validMinLength)).thenReturn(normalizedInput);
    when(moduleRepository.findAll()).thenReturn(emptyModules);

    // when
    boolean result = moduleValidationService.exists(validMinLength);

    // then
    assertFalse(result);
    verify(moduleRepository).findAll();
  }

  @Test
  void exists_shouldAcceptMaxValidModuleNameLength() {
    // given
    String validMaxLength = "A".repeat(50); // 50 characters
    String normalizedInput = "A".repeat(50);
    Iterable<ModuleEntity> emptyModules = Collections.emptyList();

    when(moduleUtilService.normalizeModuleName(validMaxLength)).thenReturn(normalizedInput);
    when(moduleRepository.findAll()).thenReturn(emptyModules);

    // when
    boolean result = moduleValidationService.exists(validMaxLength);

    // then
    assertFalse(result);
    verify(moduleRepository).findAll();
  }

  @Test
  void exists_shouldHandleCaseInsensitiveComparison() {
    // given
    String moduleInput = "math101";
    String normalizedInput = "MATH101";

    ModuleEntity module1 = new ModuleEntity();
    module1.setName("Math101"); // Different case

    Iterable<ModuleEntity> modules = Arrays.asList(module1);

    when(moduleUtilService.normalizeModuleName(moduleInput)).thenReturn(normalizedInput);
    when(moduleRepository.findAll()).thenReturn(modules);
    when(moduleUtilService.normalizeModuleName("Math101")).thenReturn("MATH101");

    // when
    boolean result = moduleValidationService.exists(moduleInput);

    // then
    assertTrue(result);
    verify(moduleUtilService).normalizeModuleName(moduleInput);
    verify(moduleRepository).findAll();
    verify(moduleUtilService).normalizeModuleName("Math101");
  }
}
