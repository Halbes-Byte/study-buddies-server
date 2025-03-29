package com.studybuddies.server.web;

import com.studybuddies.server.services.user.UserService;
import com.studybuddies.server.web.dto.UserAccountSetupRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

  UserService userService;

 @GetMapping
 public String hello(HttpServletRequest request) {
   return request.getUserPrincipal().getName();
 }

 @PostMapping
  public ResponseEntity<?> create(HttpServletRequest request,
     @RequestBody UserAccountSetupRequest userAccountSetupRequest
 ) {
     String userUUID = request.getUserPrincipal().getName();
     userService.create(userAccountSetupRequest, userUUID);
     return new ResponseEntity<>(userUUID, HttpStatus.CREATED);
 }

 @DeleteMapping
    public ResponseEntity<?> delete(HttpServletRequest request,
                                    @RequestParam UUID targetUuid) {
     userService.delete(targetUuid, request.getUserPrincipal().getName());
     return new ResponseEntity<>(targetUuid, HttpStatus.OK);
 }
}
