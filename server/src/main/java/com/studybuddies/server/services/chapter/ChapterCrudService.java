package com.studybuddies.server.services.chapter;

import com.studybuddies.server.services.interfaces.CRUDService;
import com.studybuddies.server.web.dto.chapter.ChapterCreationRequest;
import com.studybuddies.server.web.dto.interfaces.ChangeRequest;
import com.studybuddies.server.web.dto.interfaces.Responses;
import org.springframework.stereotype.Service;

@Service
public class ChapterCrudService implements CRUDService<ChapterCreationRequest, ChangeRequest, Responses> {

}
