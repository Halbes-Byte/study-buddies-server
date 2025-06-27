package com.studybuddies.server.web;

import com.studybuddies.server.services.chapter.CheckboxCrudService;
import com.studybuddies.server.web.dto.chapter.CheckboxChangeRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/checkbox")
public class CheckboxController {

  private final CheckboxCrudService checkboxCrudService;

  @PostMapping
  public ResponseEntity<?> check(HttpServletRequest r, @RequestBody
      CheckboxChangeRequest checkboxChangeRequest) {
    checkboxCrudService.update(null, checkboxChangeRequest, r.getUserPrincipal().getName());
    return new ResponseEntity<>(checkboxChangeRequest.checkboxId, HttpStatus.OK);
  }
}
