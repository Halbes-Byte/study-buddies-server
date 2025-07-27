package com.studybuddies.server.services;

import com.studybuddies.server.domain.ChapterEntity;
import com.studybuddies.server.domain.CheckboxEntity;
import com.studybuddies.server.domain.Filter;
import com.studybuddies.server.domain.UserEntity;
import com.studybuddies.server.domain.UserModule;
import com.studybuddies.server.persistance.UserRepository;
import com.studybuddies.server.services.exceptions.ModuleNotFoundException;
import com.studybuddies.server.services.exceptions.UserAccountSetupNotFinished;
import com.studybuddies.server.services.exceptions.UsernameAlreadyTakenException;
import com.studybuddies.server.services.module.ModuleValidationService;
import com.studybuddies.server.services.user.UserService;
import com.studybuddies.server.web.dto.user.AccountChangeRequest;
import com.studybuddies.server.web.dto.user.UserAccountSetupRequest;
import com.studybuddies.server.web.dto.user.UserModuleReq;
import com.studybuddies.server.web.dto.user.UserResponse;
import com.studybuddies.server.web.mapper.UserMapper;
import com.studybuddies.server.web.mapper.UserModuleMapper;
import com.studybuddies.server.web.mapper.exceptions.AccountSetupAlreadyFinished;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @Mock
  private UserModuleMapper userModuleMapper;

  @Mock
  private ModuleValidationService moduleValidationService;

  @InjectMocks
  private UserService userService;

  @Test
  void get_shouldReturnUserResponseWhenUserExists() {
    // given
    String userUUID = "550e8400-e29b-41d4-a716-446655440000";
    UUID uuid = UUID.fromString(userUUID);
    Filter filter = Filter.of(null, "test", "value");

    UserEntity userEntity = new UserEntity();
    userEntity.setUuid(uuid);
    userEntity.setUsername("testuser");

    UserResponse userResponse = new UserResponse();
    userResponse.setUuid(userUUID);
    userResponse.setUsername("testuser");

    try (MockedStatic<UUIDService> mockedUUIDService = mockStatic(UUIDService.class)) {
      mockedUUIDService.when(() -> UUIDService.parseUUID(userUUID)).thenReturn(uuid);
      when(userRepository.findById(uuid)).thenReturn(Optional.of(userEntity));
      when(userMapper.of(userEntity)).thenReturn(userResponse);

      // when
      List<UserResponse> result = userService.get(userUUID, filter);

      // then
      assertEquals(1, result.size());
      assertEquals(userUUID, result.get(0).getUuid());
      assertEquals("testuser", result.get(0).getUsername());
      verify(userRepository).findById(uuid);
      verify(userMapper).of(userEntity);
    }
  }

  @Test
  void get_shouldThrowExceptionWhenUserNotFound() {
    // given
    String userUUID = "550e8400-e29b-41d4-a716-446655440000";
    UUID uuid = UUID.fromString(userUUID);
    Filter filter = Filter.of(null, "test", "value");

    try (MockedStatic<UUIDService> mockedUUIDService = mockStatic(UUIDService.class)) {
      mockedUUIDService.when(() -> UUIDService.parseUUID(userUUID)).thenReturn(uuid);
      when(userRepository.findById(uuid)).thenReturn(Optional.empty());

      // when & then
      assertThrows(UserAccountSetupNotFinished.class, () -> {
        userService.get(userUUID, filter);
      });

      verify(userRepository).findById(uuid);
      verifyNoInteractions(userMapper);
    }
  }

  @Test
  void create_shouldCreateUserSuccessfully() {
    // given
    String userUUID = "550e8400-e29b-41d4-a716-446655440000";
    UUID uuid = UUID.fromString(userUUID);
    UserAccountSetupRequest request = new UserAccountSetupRequest();
    request.username = "newuser";

    UserEntity userEntity = new UserEntity();
    userEntity.setUsername("newuser");

    try (MockedStatic<UUIDService> mockedUUIDService = mockStatic(UUIDService.class)) {
      mockedUUIDService.when(() -> UUIDService.parseUUID(userUUID)).thenReturn(uuid);
      when(userRepository.existsById(uuid)).thenReturn(false);
      when(userRepository.existsByUsername(request.username)).thenReturn(false);
      when(userMapper.of(request)).thenReturn(userEntity);

      // when
      userService.create(request, userUUID);

      // then
      assertEquals(uuid, userEntity.getUuid());
      verify(userRepository).existsById(uuid);
      verify(userRepository).existsByUsername(request.username);
      verify(userMapper).of(request);
      verify(userRepository).save(userEntity);
    }
  }

  @Test
  void create_shouldThrowExceptionWhenUserAlreadyExists() {
    // given
    String userUUID = "550e8400-e29b-41d4-a716-446655440000";
    UUID uuid = UUID.fromString(userUUID);
    UserAccountSetupRequest request = new UserAccountSetupRequest();
    request.username = "existinguser";

    try (MockedStatic<UUIDService> mockedUUIDService = mockStatic(UUIDService.class)) {
      mockedUUIDService.when(() -> UUIDService.parseUUID(userUUID)).thenReturn(uuid);
      when(userRepository.existsById(uuid)).thenReturn(true);

      // when & then
      assertThrows(AccountSetupAlreadyFinished.class, () -> {
        userService.create(request, userUUID);
      });

      verify(userRepository).existsById(uuid);
      verifyNoInteractions(userMapper);
      verify(userRepository, never()).save(any());
    }
  }

  @Test
  void create_shouldThrowExceptionWhenUsernameAlreadyTaken() {
    // given
    String userUUID = "550e8400-e29b-41d4-a716-446655440000";
    UUID uuid = UUID.fromString(userUUID);
    UserAccountSetupRequest request = new UserAccountSetupRequest();
    request.username = "takenusername";

    try (MockedStatic<UUIDService> mockedUUIDService = mockStatic(UUIDService.class)) {
      mockedUUIDService.when(() -> UUIDService.parseUUID(userUUID)).thenReturn(uuid);
      when(userRepository.existsById(uuid)).thenReturn(false);
      when(userRepository.existsByUsername(request.username)).thenReturn(true);

      // when & then
      assertThrows(UsernameAlreadyTakenException.class, () -> {
        userService.create(request, userUUID);
      });

      verify(userRepository).existsById(uuid);
      verify(userRepository).existsByUsername(request.username);
      verifyNoInteractions(userMapper);
      verify(userRepository, never()).save(any());
    }
  }

  @Test
  void update_shouldUpdateUserWhenUserExists() {
    // given
    String targetUUID = "550e8400-e29b-41d4-a716-446655440000";
    String clientUUID = "550e8400-e29b-41d4-a716-446655440001";
    UUID uuid = UUID.fromString(targetUUID);

    AccountChangeRequest request = new AccountChangeRequest();
    request.username = "updateduser";
    request.modules = new ArrayList<>();

    UserEntity userEntity = new UserEntity();
    userEntity.setUuid(uuid);
    userEntity.setUsername("olduser");

    try (MockedStatic<UUIDService> mockedUUIDService = mockStatic(UUIDService.class)) {
      mockedUUIDService.when(() -> UUIDService.parseUUID(targetUUID)).thenReturn(uuid);
      when(userRepository.existsById(uuid)).thenReturn(true);
      when(userRepository.findById(uuid)).thenReturn(Optional.of(userEntity));

      // when
      userService.update(targetUUID, request, clientUUID);

      // then
      assertEquals("updateduser", userEntity.getUsername());
      assertEquals(request.modules, userEntity.getModules());
      verify(userRepository).existsById(uuid);
      verify(userRepository).findById(uuid);
      verify(userRepository).save(userEntity);
    }
  }

  @Test
  void update_shouldNotUpdateWhenUserDoesNotExist() {
    // given
    String targetUUID = "550e8400-e29b-41d4-a716-446655440000";
    String clientUUID = "550e8400-e29b-41d4-a716-446655440001";
    UUID uuid = UUID.fromString(targetUUID);

    AccountChangeRequest request = new AccountChangeRequest();
    request.username = "updateduser";

    try (MockedStatic<UUIDService> mockedUUIDService = mockStatic(UUIDService.class)) {
      mockedUUIDService.when(() -> UUIDService.parseUUID(targetUUID)).thenReturn(uuid);
      when(userRepository.existsById(uuid)).thenReturn(false);

      // when
      userService.update(targetUUID, request, clientUUID);

      // then
      verify(userRepository).existsById(uuid);
      verify(userRepository, never()).findById(any());
      verify(userRepository, never()).save(any());
    }
  }

  @Test
  void delete_shouldDeleteUserWhenTargetAndSenderAreSame() {
    // given
    String userUUID = "550e8400-e29b-41d4-a716-446655440000";
    UUID uuid = UUID.fromString(userUUID);

    try (MockedStatic<UUIDService> mockedUUIDService = mockStatic(UUIDService.class)) {
      mockedUUIDService.when(() -> UUIDService.parseUUID(userUUID)).thenReturn(uuid);

      // when
      userService.delete(userUUID, userUUID);

      // then
      verify(userRepository).deleteById(uuid);
    }
  }

  @Test
  void delete_shouldNotDeleteWhenTargetAndSenderAreDifferent() {
    // given
    String targetUUID = "550e8400-e29b-41d4-a716-446655440000";
    String senderUUID = "550e8400-e29b-41d4-a716-446655440001";

    // when
    userService.delete(targetUUID, senderUUID);

    // then
    verifyNoInteractions(userRepository);
  }

  @Test
  void findByUUID_shouldReturnUserWhenExists() {
    // given
    UUID uuid = UUID.randomUUID();
    UserEntity userEntity = new UserEntity();
    userEntity.setUuid(uuid);

    when(userRepository.findById(uuid)).thenReturn(Optional.of(userEntity));

    // when
    UserEntity result = userService.findByUUID(uuid);

    // then
    assertEquals(userEntity, result);
    verify(userRepository).findById(uuid);
  }

  @Test
  void findByUUID_shouldThrowExceptionWhenUserNotFound() {
    // given
    UUID uuid = UUID.randomUUID();

    when(userRepository.findById(uuid)).thenReturn(Optional.empty());

    // when & then
    assertThrows(UserAccountSetupNotFinished.class, () -> {
      userService.findByUUID(uuid);
    });

    verify(userRepository).findById(uuid);
  }

  @Test
  void existsByUUID_shouldReturnTrueWhenUserExists() {
    // given
    UUID uuid = UUID.randomUUID();

    when(userRepository.existsById(uuid)).thenReturn(true);

    // when
    boolean result = userService.existsByUUID(uuid);

    // then
    assertTrue(result);
    verify(userRepository).existsById(uuid);
  }

  @Test
  void existsByUUID_shouldReturnFalseWhenUserDoesNotExist() {
    // given
    UUID uuid = UUID.randomUUID();

    when(userRepository.existsById(uuid)).thenReturn(false);

    // when
    boolean result = userService.existsByUUID(uuid);

    // then
    assertFalse(result);
    verify(userRepository).existsById(uuid);
  }

  @Test
  void updateModules_shouldUpdateUserModulesSuccessfully() {
    // given
    String uuidStr = "550e8400-e29b-41d4-a716-446655440000";
    UUID uuid = UUID.fromString(uuidStr);

    UserModuleReq moduleReq = mock(UserModuleReq.class);
    when(moduleReq.getName()).thenReturn("MATH101");
    List<UserModuleReq> moduleUpdateRequest = Arrays.asList(moduleReq);

    UserEntity userEntity = new UserEntity();
    userEntity.setUuid(uuid);
    userEntity.setModules(new ArrayList<>());

    UserModule userModule = new UserModule();
    userModule.setName("math101");

    ChapterEntity chapter = new ChapterEntity();
    CheckboxEntity checkbox = new CheckboxEntity();
    chapter.setCheckbox(Arrays.asList(checkbox));
    userModule.setChapter(Arrays.asList(chapter));

    when(userRepository.findById(uuid)).thenReturn(Optional.of(userEntity));
    when(moduleValidationService.exists("MATH101")).thenReturn(true);
    when(userModuleMapper.of(moduleReq)).thenReturn(userModule);

    // when
    userService.updateModules(moduleUpdateRequest, uuidStr);

    // then
    assertEquals("MATH101", userModule.getName());
    assertEquals(uuid, checkbox.getUserUuid());
    assertEquals(1, userEntity.getModules().size());
    verify(userRepository).findById(uuid);
    verify(moduleValidationService).exists("MATH101");
    verify(userModuleMapper).of(moduleReq);
    verify(userRepository).save(userEntity);
  }

  @Test
  void updateModules_shouldThrowExceptionWhenUserNotFound() {
    // given
    String uuidStr = "550e8400-e29b-41d4-a716-446655440000";
    UUID uuid = UUID.fromString(uuidStr);
    List<UserModuleReq> moduleUpdateRequest = Arrays.asList();

    when(userRepository.findById(uuid)).thenReturn(Optional.empty());

    // when & then
    assertThrows(UserAccountSetupNotFinished.class, () -> {
      userService.updateModules(moduleUpdateRequest, uuidStr);
    });

    verify(userRepository).findById(uuid);
    verifyNoInteractions(moduleValidationService);
    verifyNoInteractions(userModuleMapper);
  }

  @Test
  void updateModules_shouldThrowExceptionWhenModuleNotFound() {
    // given
    String uuidStr = "550e8400-e29b-41d4-a716-446655440000";
    UUID uuid = UUID.fromString(uuidStr);

    UserModuleReq moduleReq = mock(UserModuleReq.class);
    when(moduleReq.getName()).thenReturn("NONEXISTENT");
    List<UserModuleReq> moduleUpdateRequest = Arrays.asList(moduleReq);

    UserEntity userEntity = new UserEntity();
    userEntity.setUuid(uuid);
    userEntity.setModules(new ArrayList<>());

    when(userRepository.findById(uuid)).thenReturn(Optional.of(userEntity));
    when(moduleValidationService.exists("NONEXISTENT")).thenReturn(false);

    // when & then
    assertThrows(ModuleNotFoundException.class, () -> {
      userService.updateModules(moduleUpdateRequest, uuidStr);
    });

    verify(userRepository).findById(uuid);
    verify(moduleValidationService).exists("NONEXISTENT");
    verifyNoInteractions(userModuleMapper);
    verify(userRepository, never()).save(any());
  }
}
