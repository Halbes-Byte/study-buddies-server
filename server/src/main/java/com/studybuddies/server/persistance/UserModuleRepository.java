package com.studybuddies.server.persistance;

import com.studybuddies.server.domain.UserModule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserModuleRepository extends JpaRepository<UserModule, Long> {

}
