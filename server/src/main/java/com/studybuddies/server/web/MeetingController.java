package com.studybuddies.server.web;

import com.studybuddies.server.domain.ChangeType;
import com.studybuddies.server.web.dto.MeetingChangeRequest;
import com.studybuddies.server.web.dto.MeetingCreationRequest;
import com.studybuddies.server.services.MeetingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.UUID;
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
  public ResponseEntity<?> createMeeting(
      @Valid @RequestBody MeetingCreationRequest meetingCreationRequest,
      HttpServletRequest request
  ) {
    UUID meetingId = meetingService.createMeetings(meetingCreationRequest,
        request.getUserPrincipal().getName());

    HttpHeaders returnHeader = new HttpHeaders();
    returnHeader.setLocation(URI.create("/meeting?" + meetingId));
    return new ResponseEntity<>(returnHeader, HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<?> changeMeeting(
      @RequestParam UUID id,
      @RequestParam ChangeType changeType,
      @Valid @RequestBody MeetingChangeRequest meetingChangeRequest,
      HttpServletRequest request
  ) {
    meetingService.changeMeetingInDatabase(id, meetingChangeRequest, request.getUserPrincipal().getName(), changeType);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<?> getMeeting(
      @RequestParam(required = false) UUID id
  ) {
    String response = meetingService.retrieveMeetingFromDatabase(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping
  public ResponseEntity<?> deleteMeeting(
      @RequestParam UUID id,
      HttpServletRequest request
  ) {
    meetingService.deleteMeetingFromDatabase(id, request.getUserPrincipal().getName());
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
