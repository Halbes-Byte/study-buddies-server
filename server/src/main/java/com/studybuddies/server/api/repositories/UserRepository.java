package com.studybuddies.server.api.repositories;

import java.util.UUID;

import com.studybuddies.server.api.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {

}
