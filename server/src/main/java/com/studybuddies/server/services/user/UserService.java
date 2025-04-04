package com.studybuddies.server.services.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studybuddies.server.domain.UserEntity;
import com.studybuddies.server.persistance.UserRepository;
import com.studybuddies.server.services.UUIDService;
import com.studybuddies.server.services.exceptions.UserAccountSetupNotFinished;
import com.studybuddies.server.services.interfaces.CRUDService;
import com.studybuddies.server.web.dto.AccountChangeRequest;
import com.studybuddies.server.web.dto.UserAccountSetupRequest;
import com.studybuddies.server.web.dto.UserResponse;
import com.studybuddies.server.web.mapper.UserMapper;
import com.studybuddies.server.web.mapper.exceptions.AccountSetupAlreadyFinished;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements CRUDService<UserAccountSetupRequest, AccountChangeRequest, UserResponse> {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public List<UserResponse> get(String userUUID) {
    Optional<UserEntity> target = userRepository.findById(UUIDService.parseUUID(userUUID));

    if(target.isEmpty()) throw new UserAccountSetupNotFinished("User not found");
    return List.of(userMapper.toUserResponse(target.get()));
  }

  @Override
  public void create(UserAccountSetupRequest userAccountSetupRequest, String userUUID) {
    UUID uuid = UUIDService.parseUUID(userUUID);

    if(userRepository.existsById(uuid)) {
      throw new AccountSetupAlreadyFinished("");
    }

    UserEntity user = userMapper.toUserEntity(userAccountSetupRequest);
    user.setUuid(uuid);
    userRepository.save(user);
  }

  @Override
  public void update(String targetUUID, AccountChangeRequest accountChangeRequest, String clientUUID) {
    throw new NotImplementedException("Not implemented yet");
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
}
