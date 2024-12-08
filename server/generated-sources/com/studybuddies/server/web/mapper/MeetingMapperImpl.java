package com.studybuddies.server.web.mapper;

import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.web.dto.MeetingCreationRequest;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-08T19:21:43+0100",
    comments = "version: 1.6.1, compiler: javac, environment: Java 17.0.12 (Azul Systems, Inc.)"
)
@Component
public class MeetingMapperImpl implements MeetingMapper {

    @Autowired
    private MeetingMapperUtils meetingMapperUtils;

    @Override
    public MeetingEntity MeetingCreationRequestToMeetingEntity(MeetingCreationRequest meetingCreationRequest) {
        if ( meetingCreationRequest == null ) {
            return null;
        }

        MeetingEntity.MeetingEntityBuilder meetingEntity = MeetingEntity.builder();

        meetingEntity.title( meetingCreationRequest.title );
        meetingEntity.description( meetingCreationRequest.description );
        meetingEntity.links( meetingCreationRequest.links );
        meetingEntity.place( meetingCreationRequest.place );
        meetingEntity.date_from( meetingMapperUtils.stringToLocalDate( meetingCreationRequest.date_from ) );
        meetingEntity.date_until( meetingMapperUtils.stringToLocalDate( meetingCreationRequest.date_until ) );
        meetingEntity.repeatable( meetingMapperUtils.stringToRepeatEnum( meetingCreationRequest.repeatable ) );

        MeetingEntity meetingEntityResult = meetingEntity.build();

        validate( meetingEntityResult );

        return meetingEntityResult;
    }
}
