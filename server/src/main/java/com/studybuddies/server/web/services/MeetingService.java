package com.studybuddies.server.web.services;

import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.web.dto.MeetingCreationRequest;
import com.studybuddies.server.web.mapper.MeetingMapper;
import com.studybuddies.server.web.repositories.MeetingRepository;
import org.springframework.stereotype.Service;

@Service
public class MeetingService {
    private final MeetingMapper meetingMapper;
    private final MeetingRepository meetingRepository;

    public MeetingService(MeetingMapper meetingMapper, MeetingRepository meetingRepository) {
        this.meetingMapper = meetingMapper;
        this.meetingRepository = meetingRepository;
    }

    public MeetingEntity saveMeeting(MeetingCreationRequest mcr) {
        return meetingRepository.save(meetingMapper.MeetingCreationRequestToMeetingEntity(mcr));
    }
}
