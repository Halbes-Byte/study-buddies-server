package com.studybuddies.server.services.chapter;

import com.studybuddies.server.domain.CheckboxEntity;
import com.studybuddies.server.persistance.CheckboxRepository;
import com.studybuddies.server.services.UUIDService;
import com.studybuddies.server.services.interfaces.CRUDService;
import com.studybuddies.server.web.dto.chapter.CheckboxChangeRequest;
import com.studybuddies.server.web.dto.chapter.CheckboxCreationRequest;
import com.studybuddies.server.web.dto.interfaces.Responses;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CheckboxCrudService implements CRUDService<CheckboxCreationRequest, CheckboxChangeRequest, Responses> {

    private final CheckboxRepository checkboxRepository;

    @Override
    public void update(String targetUUID, CheckboxChangeRequest r, String clientUUID) {
        var checkboxes = checkboxRepository.findByUserUuid(UUIDService.parseUUID(clientUUID));

        for(CheckboxEntity checkbox : checkboxes) {
            var checkboxId = checkbox.getId().toString();
            if(checkboxId.equals(r.checkboxId)) {
                checkbox.setChecked(!checkbox.isChecked());
                checkboxRepository.save(checkbox);
            }
        }
    }
}
