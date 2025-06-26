package com.studybuddies.server.services.chapter;

import com.studybuddies.server.persistance.ChapterRepository;
import com.studybuddies.server.services.UUIDService;
import com.studybuddies.server.services.interfaces.CRUDService;
import com.studybuddies.server.web.dto.chapter.ChapterCreationRequest;
import com.studybuddies.server.web.dto.interfaces.ChangeRequest;
import com.studybuddies.server.web.dto.interfaces.Responses;
import com.studybuddies.server.web.mapper.ChapterMapper;
import org.springframework.stereotype.Service;

@Service
public class ChapterCrudService implements CRUDService<ChapterCreationRequest, ChangeRequest, Responses> {
    private ChapterRepository chapterRepository;
    private ChapterMapper chapterMapper;

    @Override
    public void create(ChapterCreationRequest request, String clientUUID) {
        chapterRepository.save(chapterMapper.of(request));
    }

    @Override
    public void delete(String targetUUID, String clientUUID) {
        chapterRepository.deleteById(UUIDService.parseUUID(targetUUID));
    }
}
