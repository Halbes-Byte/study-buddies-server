package com.studybuddies.server.web;

import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.domain.UserEntity;
import com.studybuddies.server.services.StudyGroupService;
import com.studybuddies.server.services.UUIDService;
import com.studybuddies.server.services.meeting.MeetingService;
import com.studybuddies.server.services.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/studygroup")
public class StudyGroupController {

    private final StudyGroupService studyGroupService;
    private final MeetingService meetingService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(required = false) String meetingUUID,
                                 @RequestParam(required = false) String userUUID){
        if (meetingUUID != null) {
            return new ResponseEntity<>(studyGroupService.findByMeeting(meetingUUID), HttpStatus.OK);
        } else if (userUUID != null) {
            return new ResponseEntity<>(studyGroupService.findByUser(userUUID), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping
    public ResponseEntity<?> add(String meetingUUID, String userUUID) {
        UserEntity userEntity = userService.findByUUID(UUIDService.parseUUID(userUUID));
        MeetingEntity meetingEntity = meetingService.findMeetingByUUID(meetingUUID);
        studyGroupService.joinUserToMeeting(userEntity, meetingEntity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam(required = false) String meetingUUID,
                                 @RequestParam(required = false) String userUUID){
        if (meetingUUID != null) {
            studyGroupService.deleteByMeeting(meetingUUID);
        } else if (userUUID != null) {
            studyGroupService.deleteByUser(userUUID);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
