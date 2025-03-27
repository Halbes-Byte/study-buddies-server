package com.studybuddies.server.services;

import com.studybuddies.server.domain.UserEntity;
import com.studybuddies.server.persistance.UserRepository;
import com.studybuddies.server.services.exceptions.UserAccountSetupNotFinished;
import com.studybuddies.server.web.dto.UserAccountSetupRequest;
import com.studybuddies.server.web.mapper.UserMapper;
import com.studybuddies.server.web.mapper.exceptions.AccountSetupAlreadyFinished;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Transactional
  public UserEntity findByUUID(UUID uuid) {
    Optional<UserEntity> target = userRepository.findById(uuid);
    return target.orElseThrow(() -> new UserAccountSetupNotFinished(""));
  }

  @Transactional
  public void createUser(String uuidString, UserAccountSetupRequest userAccountSetupRequest) {
    UUID uuid = UUIDService.parseUUID(uuidString);

    if(userRepository.existsById(uuid)) {
      throw new AccountSetupAlreadyFinished("");
    }

    UserEntity user = userMapper.toUserEntity(userAccountSetupRequest);
    user.setUuid(uuid);
    userRepository.save(user);
  }

  @Transactional
  public void deleteUser(String targetUuid, String senderUuid) {
    // Note: in the future it is required that an admin user is able to delete other non admin user accounts
    if (/*(*/targetUuid.equals(senderUuid) /* || isAdmin(sender) && !isAdmin(targetUuid))*/) {
      userRepository.deleteById(UUIDService.parseUUID(targetUuid));
    }
  }

  @Transactional
  public boolean existsByUUID(UUID uuid) {
    return userRepository.existsById(uuid);
  }
}
