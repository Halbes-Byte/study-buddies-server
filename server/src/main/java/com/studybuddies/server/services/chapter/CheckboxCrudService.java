package com.studybuddies.server.services.chapter;

import com.studybuddies.server.domain.CheckboxEntity;
import com.studybuddies.server.persistance.CheckboxRepository;
import com.studybuddies.server.services.UUIDService;
import com.studybuddies.server.services.interfaces.CRUDService;
import com.studybuddies.server.web.dto.chapter.CheckboxChangeRequest;
import com.studybuddies.server.web.dto.chapter.CheckboxCreationRequest;
import com.studybuddies.server.web.dto.interfaces.ChangeRequest;
import com.studybuddies.server.web.dto.interfaces.Responses;
import com.studybuddies.server.web.mapper.CheckboxMapper;

import java.util.Optional;
import java.util.UUID;

public class CheckboxCrudService implements CRUDService<CheckboxCreationRequest, CheckboxChangeRequest, Responses> {
    private CheckboxRepository checkboxRepository;
    private CheckboxMapper checkboxMapper;

    @Override
    public void update(String targetUUID, CheckboxChangeRequest r, String clientUUID) {




    }


}
