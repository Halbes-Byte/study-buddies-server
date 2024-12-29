package com.studybuddies.server.web;

import com.studybuddies.server.web.dto.MeetingChangeRequest;
import com.studybuddies.server.web.dto.MeetingCreationRequest;
import com.studybuddies.server.services.MeetingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/meeting")
public class MeetingController {
  private final MeetingService meetingService;

  @PostMapping
  public ResponseEntity<?> createMeeting(@Valid @RequestBody MeetingCreationRequest meetingCreationRequest) {
    meetingService.saveMeeting(meetingCreationRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<?> changeMeeting(@Valid @RequestBody long id, MeetingChangeRequest meetingChangeRequest) {
    meetingService.changeMeetingInDatabase(id, meetingChangeRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
