package com.studybuddies.server.web;

import com.studybuddies.server.services.user.UserService;
import com.studybuddies.server.web.dto.user.ModuleUpdateRequest;
import com.studybuddies.server.web.dto.user.UserAccountSetupRequest;
import com.studybuddies.server.web.dto.user.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
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
  public List<UserResponse> get(HttpServletRequest request) {
    var uuid = request.getUserPrincipal().getName();
    return userService.get(uuid);
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
      @RequestParam String targetUuid) {
    userService.delete(targetUuid, request.getUserPrincipal().getName());
    return new ResponseEntity<>(targetUuid, HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<?> put(HttpServletRequest request, @RequestBody ModuleUpdateRequest updateRequest) {
    String userUUID = request.getUserPrincipal().getName();
    userService.updateModules(updateRequest, userUUID);
    return new ResponseEntity<>(userUUID, HttpStatus.OK);
  }
}
