/*
package com.studybuddies.server.web;

import com.studybuddies.server.services.chapter.ChapterCrudService;
import com.studybuddies.server.web.dto.chapter.ChapterCreationRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/chapter")
public class ChapterController {

    private ChapterCrudService chapterCrudService;

    @PostMapping
    public ResponseEntity<?> create(HttpServletRequest request,
                                    @RequestBody ChapterCreationRequest chapterCreationRequest
                                    ) {
        String userUUID = request.getUserPrincipal().getName();
        chapterCrudService.create(chapterCreationRequest, userUUID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(HttpServletRequest request,
                                    @RequestParam String targetUuid) {
        String userUUID = request.getUserPrincipal().getName();
        chapterCrudService.delete(targetUuid, userUUID);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
*/
