package com.studybuddies.server.web.dto.user;

import com.studybuddies.server.domain.UserModule;
import com.studybuddies.server.web.dto.interfaces.ChangeRequest;

import java.util.List;

public class AccountChangeRequest implements ChangeRequest {
    public String id;
    public String username;
    public List<UserModule> modules;
}