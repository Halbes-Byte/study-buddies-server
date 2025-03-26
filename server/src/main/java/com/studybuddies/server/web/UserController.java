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
     UUID uuid = UUIDService.parseUUID(request.getUserPrincipal().getName());
     userService.createUser(uuid, userAccountSetupRequest);
     return new ResponseEntity<>(uuid.toString(), HttpStatus.CREATED);
 }

 @DeleteMapping
    public ResponseEntity<?> delete(HttpServletRequest request,
                                    @RequestParam UUID uuid,
                                    @RequestBody UserAccountSetupRequest userAccountSetupRequest) {
     userService.deleteUser(uuid, UUIDService.parseUUID(request.getUserPrincipal().getName()));
     return new ResponseEntity<>(uuid, HttpStatus.OK);
 }
}
