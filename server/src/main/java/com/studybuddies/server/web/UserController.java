package com.studybuddies.server.web;

import com.studybuddies.server.services.UserService;
import com.studybuddies.server.web.dto.UserAccountSetupRequest;
import jakarta.servlet.http.HttpServletRequest;
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
     String uuidString = request.getUserPrincipal().getName();
     userService.createUser(uuidString, userAccountSetupRequest);
     return new ResponseEntity<>(uuidString, HttpStatus.CREATED);
 }

 @DeleteMapping
    public ResponseEntity<?> delete(HttpServletRequest request,
                                    @RequestParam String targetUuid,
                                    @RequestBody UserAccountSetupRequest userAccountSetupRequest) {
     userService.deleteUser(targetUuid, request.getUserPrincipal().getName());
     return new ResponseEntity<>(targetUuid, HttpStatus.OK);
 }
}
