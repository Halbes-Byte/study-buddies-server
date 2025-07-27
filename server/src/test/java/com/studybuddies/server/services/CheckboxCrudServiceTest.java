package com.studybuddies.server.services;

import com.studybuddies.server.domain.CheckboxEntity;
import com.studybuddies.server.persistance.CheckboxRepository;
import com.studybuddies.server.services.chapter.CheckboxCrudService;
import com.studybuddies.server.web.dto.chapter.CheckboxChangeRequest;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckboxCrudServiceTest {

  @Mock
  private CheckboxRepository checkboxRepository;

  @InjectMocks
  private CheckboxCrudService checkboxCrudService;

  @Test
  void update_CheckboxFoundAndToggled_Success() {
    // given
    String targetUUID = UUID.randomUUID().toString();
    String clientUUID = UUID.randomUUID().toString();
    String checkboxId = UUID.randomUUID().toString();
    CheckboxChangeRequest request = new CheckboxChangeRequest();
    request.checkboxId = checkboxId;

    CheckboxEntity checkbox = new CheckboxEntity();
    checkbox.setId(UUID.fromString(checkboxId));
    checkbox.setChecked(false);

    List<CheckboxEntity> checkboxes = List.of(checkbox);

    when(checkboxRepository.findByUserUuid(any(UUID.class))).thenReturn(checkboxes);

    // when
    checkboxCrudService.update(targetUUID, request, clientUUID);

    // then
    verify(checkboxRepository).save(checkbox);
    assertTrue(checkbox.isChecked());
  }

  @Test
  void update_MultipleCheckboxesOnlyCorrectOneToggled_Success() {
    // given
    String targetUUID = UUID.randomUUID().toString();
    String clientUUID = UUID.randomUUID().toString();
    String targetCheckboxId = UUID.randomUUID().toString();
    CheckboxChangeRequest request = new CheckboxChangeRequest();
    request.checkboxId = targetCheckboxId;

    CheckboxEntity checkbox1 = new CheckboxEntity();
    checkbox1.setId(UUID.fromString(targetCheckboxId));
    checkbox1.setChecked(false);

    CheckboxEntity checkbox2 = new CheckboxEntity();
    checkbox2.setId(UUID.randomUUID());
    checkbox2.setChecked(true);

    List<CheckboxEntity> checkboxes = List.of(checkbox1, checkbox2);

    when(checkboxRepository.findByUserUuid(any(UUID.class))).thenReturn(checkboxes);

    // when
    checkboxCrudService.update(targetUUID, request, clientUUID);

    // then
    verify(checkboxRepository).save(checkbox1);
    verify(checkboxRepository, never()).save(checkbox2);
    assertTrue(checkbox1.isChecked());
    assertTrue(checkbox2.isChecked());
  }

  @Test
  void update_CheckboxNotFound_NoSaveCall() {
    // given
    String targetUUID = UUID.randomUUID().toString();
    String clientUUID = UUID.randomUUID().toString();
    String nonExistentCheckboxId = "non-existent-id";
    CheckboxChangeRequest request = new CheckboxChangeRequest();
    request.checkboxId = nonExistentCheckboxId;

    CheckboxEntity checkbox = new CheckboxEntity();
    checkbox.setId(UUID.randomUUID());
    checkbox.setChecked(false);

    List<CheckboxEntity> checkboxes = List.of(checkbox);

    when(checkboxRepository.findByUserUuid(any(UUID.class))).thenReturn(checkboxes);

    // when
    checkboxCrudService.update(targetUUID, request, clientUUID);

    // then
    verify(checkboxRepository, never()).save(any());
    assertFalse(checkbox.isChecked());
  }

  @Test
  void update_EmptyCheckboxList_NoSaveCall() {
    // given
    String targetUUID = UUID.randomUUID().toString();
    String clientUUID = UUID.randomUUID().toString();
    CheckboxChangeRequest request = new CheckboxChangeRequest();
    request.checkboxId = "any-id";

    List<CheckboxEntity> emptyCheckboxes = List.of();

    when(checkboxRepository.findByUserUuid(any(UUID.class))).thenReturn(emptyCheckboxes);

    // when
    checkboxCrudService.update(targetUUID, request, clientUUID);

    // then
    verify(checkboxRepository, never()).save(any());
  }

  @Test
  void update_CheckboxToggleFromTrueToFalse_Success() {
    // given
    String targetUUID = UUID.randomUUID().toString();
    String clientUUID = UUID.randomUUID().toString();
    String checkboxId = UUID.randomUUID().toString();
    CheckboxChangeRequest request = new CheckboxChangeRequest();
    request.checkboxId = checkboxId;

    CheckboxEntity checkbox = new CheckboxEntity();
    checkbox.setId(UUID.fromString(checkboxId));
    checkbox.setChecked(true);

    List<CheckboxEntity> checkboxes = List.of(checkbox);

    when(checkboxRepository.findByUserUuid(any(UUID.class))).thenReturn(checkboxes);

    // when
    checkboxCrudService.update(targetUUID, request, clientUUID);

    // then
    verify(checkboxRepository).save(checkbox);
    assertFalse(checkbox.isChecked());
  }
}
