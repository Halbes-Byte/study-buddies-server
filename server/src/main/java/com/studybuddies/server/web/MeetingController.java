package com.studybuddies.server.web;

import com.studybuddies.server.web.dto.MeetingCreationRequest;
import com.studybuddies.server.web.services.MeetingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
