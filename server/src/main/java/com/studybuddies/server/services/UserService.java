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

  // todo
  @Transactional
  public void deleteUser(String targetUuid, String sender) {
    if (targetUuid.equals(sender) /* || isAdmin(sender)*/) {
      userRepository.deleteById(UUIDService.parseUUID(targetUuid));
    }
  }
}
