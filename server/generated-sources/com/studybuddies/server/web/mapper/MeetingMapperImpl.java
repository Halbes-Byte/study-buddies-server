package com.studybuddies.server.web.mapper;

import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.domain.Repeat;
import com.studybuddies.server.web.dto.MeetingChangeRequest;
import com.studybuddies.server.web.dto.MeetingCreationRequest;
import com.studybuddies.server.web.dto.MeetingResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-05T10:45:41+0100",
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

    @Override
    public MeetingEntity MeetingChangeRequestToMeetingEntity(MeetingChangeRequest meetingChangeRequest) {
        if ( meetingChangeRequest == null ) {
            return null;
        }

        MeetingEntity.MeetingEntityBuilder meetingEntity = MeetingEntity.builder();

        meetingEntity.title( meetingChangeRequest.title );
        meetingEntity.description( meetingChangeRequest.description );
        meetingEntity.links( meetingChangeRequest.links );
        if ( meetingChangeRequest.date_from != null ) {
            meetingEntity.date_from( LocalDateTime.parse( meetingChangeRequest.date_from ) );
        }
        if ( meetingChangeRequest.date_until != null ) {
            meetingEntity.date_until( LocalDateTime.parse( meetingChangeRequest.date_until ) );
        }
        if ( meetingChangeRequest.repeatable != null ) {
            meetingEntity.repeatable( Enum.valueOf( Repeat.class, meetingChangeRequest.repeatable ) );
        }
        meetingEntity.place( meetingChangeRequest.place );

        MeetingEntity meetingEntityResult = meetingEntity.build();

        validate( meetingEntityResult );

        return meetingEntityResult;
    }

    @Override
    public MeetingResponse MeetingEntityToMeetingResponse(MeetingEntity meetingEntity) {
        if ( meetingEntity == null ) {
            return null;
        }

        MeetingResponse meetingResponse = new MeetingResponse();

        meetingResponse.title = meetingEntity.getTitle();
        meetingResponse.description = meetingEntity.getDescription();
        meetingResponse.links = meetingEntity.getLinks();
        if ( meetingEntity.getDate_from() != null ) {
            meetingResponse.date_from = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( meetingEntity.getDate_from() );
        }
        if ( meetingEntity.getDate_until() != null ) {
            meetingResponse.date_until = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( meetingEntity.getDate_until() );
        }
        if ( meetingEntity.getRepeatable() != null ) {
            meetingResponse.repeatable = meetingEntity.getRepeatable().name();
        }
        meetingResponse.place = meetingEntity.getPlace();

        return meetingResponse;
    }
}
