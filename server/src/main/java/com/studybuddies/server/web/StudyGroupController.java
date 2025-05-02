package com.studybuddies.server.web;

import com.studybuddies.server.services.StudyGroupService;
import com.studybuddies.server.web.dto.studygroup.StudyGroupJoinRequest;
import jakarta.servlet.http.HttpServletRequest;
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
  public ResponseEntity<?> get(String uuid) {
    return new ResponseEntity<>(studyGroupService.get(uuid), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<?> add(
      @RequestBody StudyGroupJoinRequest studyGroupJoinRequest,
      HttpServletRequest request
  ) {
    studyGroupService.create(studyGroupJoinRequest, request.getUserPrincipal().getName());
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping
  public ResponseEntity<?> delete(String targetUUID, HttpServletRequest request) {
    studyGroupService.delete(targetUUID, request.getUserPrincipal().getName());
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
