package com.studybuddies.server.services.user;

import com.studybuddies.server.domain.ChapterEntity;
import com.studybuddies.server.domain.CheckboxEntity;
import com.studybuddies.server.domain.Filter;
import com.studybuddies.server.domain.UserEntity;
import com.studybuddies.server.domain.UserModule;
import com.studybuddies.server.persistance.CheckboxRepository;
import com.studybuddies.server.persistance.UserModuleRepository;
import com.studybuddies.server.persistance.UserRepository;
import com.studybuddies.server.services.UUIDService;
import com.studybuddies.server.services.exceptions.ModuleNotFoundException;
import com.studybuddies.server.services.exceptions.UserAccountSetupNotFinished;
import com.studybuddies.server.services.exceptions.UsernameAlreadyTakenException;
import com.studybuddies.server.services.interfaces.CRUDService;
import com.studybuddies.server.services.module.ModuleValidationService;
import com.studybuddies.server.web.dto.chapter.ChapterCreationRequest;
import com.studybuddies.server.web.dto.user.AccountChangeRequest;
import com.studybuddies.server.web.dto.user.UserAccountSetupRequest;
import com.studybuddies.server.web.dto.user.UserModuleReq;
import com.studybuddies.server.web.dto.user.UserResponse;
import com.studybuddies.server.web.mapper.ChapterMapper;
import com.studybuddies.server.web.mapper.UserMapper;
import com.studybuddies.server.web.mapper.UserModuleMapper;
import com.studybuddies.server.web.mapper.exceptions.AccountSetupAlreadyFinished;
import jakarta.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PatchMapping;

@Service
@AllArgsConstructor
public class UserService implements
    CRUDService<UserAccountSetupRequest, AccountChangeRequest, UserResponse> {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final UserModuleMapper userModuleMapper;
  private final ModuleValidationService moduleValidationService;
  private final UserModuleRepository userModuleRepository;
  private final ChapterMapper chapterMapper;
  private final CheckboxRepository checkboxRepository;

  @Override
  public List<UserResponse> get(String userUUID, Filter filter) {
    Optional<UserEntity> target = userRepository.findById(UUIDService.parseUUID(userUUID));

    if (target.isEmpty()) {
      throw new UserAccountSetupNotFinished("User not found");
    }
    return List.of(userMapper.of(target.get()));
  }

  @Override
  public void create(UserAccountSetupRequest userAccountSetupRequest, String userUUID) {
    UUID uuid = UUIDService.parseUUID(userUUID);

    if (userRepository.existsById(uuid)) {
      throw new AccountSetupAlreadyFinished("");
    }

    if (userRepository.existsByUsername(userAccountSetupRequest.username)) {
      throw new UsernameAlreadyTakenException("");
    }

    UserEntity user = userMapper.of(userAccountSetupRequest);
    user.setUuid(uuid);
    userRepository.save(user);
  }

  public void patch(List<UserModuleReq> requestList, String userUUID) {
      Optional<UserEntity> maybeUser = userRepository.findById(UUIDService.parseUUID(userUUID));

      if (maybeUser.isPresent()) {
          UserEntity user = maybeUser.get();
          List<UserModule> userModules = user.getModules();

          List<CheckboxEntity> userCheckboxes = userModules.stream()
                  .flatMap(module -> module.getChapter().stream())
                  .flatMap(chapter -> chapter.getCheckbox().stream())
                  .toList();

          List<CheckboxEntity> reqCheckboxes = requestList.stream()
                  .flatMap(module -> Arrays.stream(module.getChapter()))
                  .flatMap(chapterEntityRequest -> chapterMapper.of(chapterEntityRequest).getCheckbox().stream())
                  .toList();

        for (CheckboxEntity userCheckbox : userCheckboxes) {
          for (CheckboxEntity reqCheckBox : reqCheckboxes) {
            if (userCheckbox.getId() == reqCheckBox.getId()) {
              CheckboxEntity candidate = checkboxRepository.findById(userCheckbox.getId()).get();
              candidate.setChecked(!candidate.isChecked());
              checkboxRepository.save(candidate);
            }
          }
        }
      }
  }

  @Override
  public void update(String targetUUID, AccountChangeRequest accountChangeRequest,
      String clientUUID) {
    UUID uuid = UUIDService.parseUUID(targetUUID);
    if(existsByUUID(uuid)) {
      UserEntity user = userRepository.findById(uuid).get();
      user.setModules(accountChangeRequest.modules);
      user.setUsername(accountChangeRequest.username);
      userRepository.save(user);
    }
  }

  @Override
  public void delete(String targetUuid, String senderUuid) {
    // Note: in the future it is required that an admin user is able to delete other non admin user accounts
    if (/*(*/targetUuid.equals(senderUuid) /* || isAdmin(sender) && !isAdmin(targetUuid))*/) {
      userRepository.deleteById(UUIDService.parseUUID(targetUuid));
    }
  }

  @Transactional
  public UserEntity findByUUID(UUID uuid) {
    Optional<UserEntity> target = userRepository.findById(uuid);
    return target.orElseThrow(() -> new UserAccountSetupNotFinished(""));
  }


  @Transactional
  public boolean existsByUUID(UUID uuid) {
    return userRepository.existsById(uuid);
  }

  @Transactional
  public void updateModules(List<UserModuleReq> moduleUpdateRequest, String uuidStr) {
    UUID uuid = UUID.fromString(uuidStr);
    UserEntity user = userRepository.findById(uuid)
        .orElseThrow(() -> new UserAccountSetupNotFinished("User not found"));

    user.getModules().clear();

    for (UserModuleReq req : moduleUpdateRequest) {
      if (!moduleValidationService.exists(req.getName())) throw new ModuleNotFoundException("");

      UserModule module = userModuleMapper.of(req);
      module.setUser(user);

      if (module.getChapter() != null) {
        for (ChapterEntity chapter : module.getChapter()) {
          module.setName(module.getName().toUpperCase());
          chapter.setModule(module);

          if (chapter.getCheckbox() != null) {
            for (CheckboxEntity cb : chapter.getCheckbox()) {
              cb.setChapter(chapter);
            }
          }
        }
      }

      user.getModules().add(module);
      userModuleRepository.save(module);
    }
    userRepository.save(user);
  }
}
