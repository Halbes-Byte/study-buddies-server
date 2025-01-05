package com.studybuddies.server.web;

import com.studybuddies.server.web.dto.MeetingChangeRequest;
import com.studybuddies.server.web.dto.MeetingCreationRequest;
import com.studybuddies.server.services.MeetingService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    Long meetingId = meetingService.saveMeetingToDatabase(meetingCreationRequest);
    HttpHeaders returnHeader = new HttpHeaders();
    returnHeader.setLocation(URI.create("/meeting?" + meetingId));
    return new ResponseEntity<>(returnHeader, HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<?> changeMeeting(@RequestParam Long id, @Valid @RequestBody MeetingChangeRequest meetingChangeRequest) {
    meetingService.changeMeetingInDatabase(id, meetingChangeRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<?> getMeeting(@RequestParam(required = false) Long id) {
    String response = meetingService.retrieveMeetingFromDatabase(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping
  public ResponseEntity<?> deleteMeeting(@RequestParam Long id) {
    meetingService.deleteMeetingFromDatabase(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
