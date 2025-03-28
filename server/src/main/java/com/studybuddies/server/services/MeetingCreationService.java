package com.studybuddies.server.services;

import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.domain.Repeat;
import com.studybuddies.server.domain.UserEntity;
import com.studybuddies.server.web.dto.MeetingCreationRequest;
import com.studybuddies.server.web.mapper.MeetingMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingCreationService {
  private final MeetingMapper meetingMapper;
  private final UserService userService;

  private LocalDateTime endTime;

  public List<MeetingEntity> createMeetingInstances(
      MeetingCreationRequest meetingCreationRequest,
      String uuid
  ) {
    List<MeetingEntity> meetingEntities = new ArrayList<>();
    setEndTime();

    UUID superUUID = UUID.randomUUID();

    MeetingEntity baseMeeting = meetingMapper.meetingCreationRequestToMeetingEntity(meetingCreationRequest);
    UserEntity creator = userService.findByUUID(UUIDService.parseUUID(uuid));
    baseMeeting.setCreator(creator);
    baseMeeting.setSuperId(superUUID);
    meetingEntities.add(baseMeeting);

    while(shouldCreateMeeting(baseMeeting)) {
      MeetingEntity newMeeting = cloneMeetingEntity(baseMeeting);
      updateMeeting(newMeeting);
      meetingEntities.add(newMeeting);
      baseMeeting = newMeeting;
    }

    return meetingEntities;
  }

  private void updateMeeting(MeetingEntity meetingEntity) {
    LocalDateTime startDate = meetingEntity.getDate_from();
    LocalDateTime endDate = meetingEntity.getDate_until();

    switch(meetingEntity.getRepeatable()) {
      case DAILY:
        meetingEntity.setDate_from(startDate.plusDays(1));
        meetingEntity.setDate_until(endDate.plusDays(1));
        break;
      case WEEKLY:
        meetingEntity.setDate_from(startDate.plusWeeks(1));
        meetingEntity.setDate_until(endDate.plusWeeks(1));
        break;
      case MONTHLY:
        meetingEntity.setDate_from(startDate.plusMonths(1));
        meetingEntity.setDate_until(endDate.plusMonths(1));
        break;
      default:
        throw new IllegalArgumentException("Invalid repeatable");
    }
  }

  private boolean shouldCreateMeeting(MeetingEntity meetingEntity) {
    if(meetingEntity.getRepeatable() == Repeat.NEVER) {
      return false;
    }
    return meetingEntity.getDate_from().isBefore(endTime);

  }

  private void setEndTime() {
    LocalDate now = LocalDate.now();
    Month endMonth;

    boolean semester =
        now.getMonth().compareTo(Month.FEBRUARY) < 0 || now.getMonth().compareTo(Month.OCTOBER) > 0;

    if(semester) {
      endMonth = Month.FEBRUARY;
    } else {
      endMonth = Month.OCTOBER;
    }

    endTime = LocalDate.of(LocalDate.now().getYear(), endMonth.getValue(), 1).atStartOfDay();
  }


  private MeetingEntity cloneMeetingEntity(MeetingEntity meetingEntity) {
    return MeetingEntity.builder()
        .superId(meetingEntity.getSuperId())
        .title(meetingEntity.getTitle())
        .description(meetingEntity.getDescription())
        .date_from(meetingEntity.getDate_from())
        .date_until(meetingEntity.getDate_until())
        .repeatable(meetingEntity.getRepeatable())
        .place(meetingEntity.getPlace())
        .creator(meetingEntity.getCreator())
        .build();
  }
}
