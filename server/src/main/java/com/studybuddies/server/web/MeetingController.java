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
    return new ResponseEntity<>(meetingService.saveMeetingToDatabase(meetingCreationRequest), HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<?> changeMeeting(long id, @Valid @RequestBody MeetingChangeRequest meetingChangeRequest) {
    meetingService.changeMeetingInDatabase(id, meetingChangeRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<?> getMeeting(long id) {
    return new ResponseEntity<>(meetingService.retrieveMeetingFromDatabase(id), HttpStatus.FOUND);
  }

  @DeleteMapping
  public ResponseEntity<?> deleteMeeting(long id) {
    meetingService.deleteMeetingFromDatabase(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
