package com.studybuddies.server.web;

import com.studybuddies.server.services.StudyGroupService;
import com.studybuddies.server.web.dto.StudyGroupJoinRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/studygroup")
public class StudyGroupController {

    private final StudyGroupService studyGroupService;

    @GetMapping
    public ResponseEntity<?> get(@RequestParam String uuid) {
        return new ResponseEntity<>(studyGroupService.get(uuid), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> add(StudyGroupJoinRequest studyGroupJoinRequest, String clientUUID) {
        studyGroupService.create(studyGroupJoinRequest, clientUUID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(String targetUUID, String clientUUID){
        studyGroupService.delete(targetUUID, clientUUID);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
