package com.studybuddies.server.web;

import com.studybuddies.server.web.dto.MeetingCreationRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meeting")
public class MeetingController {

  @PostMapping
  public ResponseEntity<?> createMeeting(@Valid @RequestBody MeetingCreationRequest meetingCreationRequest) {
    //
    // MeetingCreationService(
    //    meetingCreationRequest -> entity
    //    DBSave(entity)
    // )
    // Response: OK
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
