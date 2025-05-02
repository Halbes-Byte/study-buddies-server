package com.studybuddies.server.web;

import com.studybuddies.server.services.module.ModuleCrudService;
import com.studybuddies.server.web.dto.module.ModuleCreationRequest;
import com.studybuddies.server.web.dto.module.ModuleResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/module")
@RequiredArgsConstructor
public class ModuleController {

  public final ModuleCrudService moduleCrudService;

  @GetMapping
  public List<ModuleResponse> get() {
    return moduleCrudService.get();
  }

  @PostMapping
  public ResponseEntity<?> create(@Valid @RequestBody ModuleCreationRequest moduleCreationRequest) {
    moduleCrudService.create(moduleCreationRequest, null);
    return ResponseEntity.status(HttpStatus.CREATED).body("Module created");
  }
}
