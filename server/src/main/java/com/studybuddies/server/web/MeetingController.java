package com.studybuddies.server.web;

import com.studybuddies.server.web.dto.meeting.MeetingChangeRequest;
import com.studybuddies.server.web.dto.meeting.MeetingCreationRequest;
import com.studybuddies.server.services.meeting.MeetingService;
import com.studybuddies.server.web.dto.meeting.MeetingResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
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

  @GetMapping
  public ResponseEntity<?> get(
      @RequestParam(required = false) String id
  ) {
    List<MeetingResponse> response = meetingService.get(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping
  public ResponseEntity<?> create(
      @Valid @RequestBody MeetingCreationRequest meetingCreationRequest,
      HttpServletRequest request
  ) {
    meetingService.create(meetingCreationRequest,
        request.getUserPrincipal().getName());

    HttpHeaders returnHeader = new HttpHeaders();
    returnHeader.setLocation(URI.create("/meeting"));
    return new ResponseEntity<>(returnHeader, HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<?> update(
      @RequestParam String id,
      @Valid @RequestBody MeetingChangeRequest meetingChangeRequest,
      HttpServletRequest request
  ) {
    meetingService.update(id, meetingChangeRequest, request.getUserPrincipal().getName());
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping
  public ResponseEntity<?> delete(
      @RequestParam String id,
      HttpServletRequest request
  ) {
    meetingService.delete(id, request.getUserPrincipal().getName());
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
