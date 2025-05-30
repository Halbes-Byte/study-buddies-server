package com.studybuddies.server.web;

import com.studybuddies.server.domain.Filter;
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
      @RequestParam(required = false) String module,
      HttpServletRequest request
  ) {

    List<MeetingResponse> response;
    if(module != null)
      response = meetingService.get(request.getUserPrincipal().getName(), Filter.of(null, "module", module));
    else
      response = meetingService.get(request.getUserPrincipal().getName(), null);
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
