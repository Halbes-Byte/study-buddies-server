/*
package com.studybuddies.server.web;

import com.studybuddies.server.services.chapter.ChapterCrudService;
import com.studybuddies.server.services.chapter.CheckboxCrudService;
import com.studybuddies.server.web.dto.chapter.ChapterCreationRequest;
import com.studybuddies.server.web.dto.chapter.CheckboxCreationRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/chapter")
public class CheckboxController {
    private CheckboxCrudService checkboxCrudService;

    @PostMapping
    public ResponseEntity<?> create(HttpServletRequest request,
                                    @RequestBody CheckboxCreationRequest checkboxCreationRequest
                                    ) {
        String userUUID = request.getUserPrincipal().getName();
        checkboxCrudService.create(checkboxCreationRequest, userUUID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> update(HttpServletRequest request,
                                    @RequestBody CheckboxCreationRequest checkboxCreationRequest
    ) {
        String userUUID = request.getUserPrincipal().getName();

    }

    @DeleteMapping
    public ResponseEntity<?> delete(HttpServletRequest request,
                                    @RequestParam String targetUuid) {
        String userUUID = request.getUserPrincipal().getName();
        checkboxCrudService.delete(targetUuid, userUUID);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
*/
