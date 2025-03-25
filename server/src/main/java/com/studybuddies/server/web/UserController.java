package com.studybuddies.server.web;

import com.studybuddies.server.services.UUIDService;
import com.studybuddies.server.services.UserService;
import com.studybuddies.server.web.dto.UserAccountSetupRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     UUID uuid = UUIDService.parseUUID(request.getUserPrincipal().getName());
     userService.createUser(uuid, userAccountSetupRequest);
     return new ResponseEntity<>(uuid.toString(), HttpStatus.CREATED);
 }
}
