package com.studybuddies.server.services.meeting;

import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.domain.Repeat;
import com.studybuddies.server.domain.UserEntity;
import com.studybuddies.server.persistance.MeetingRepository;
import com.studybuddies.server.services.UUIDService;
import com.studybuddies.server.services.user.UserService;
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
  private final MeetingRepository meetingRepository;

  private LocalDateTime endTime;

  public void createMeetingInstances(
      MeetingCreationRequest meetingCreationRequest,
      String creatorUuid,
      UUID superUUID,
      boolean isUpdate
  ) {
    List<MeetingEntity> meetingEntities = new ArrayList<>();
    setEndTime();

    if(superUUID == null) {
      superUUID = UUID.randomUUID();
    }

    MeetingEntity baseMeeting = meetingMapper.meetingCreationRequestToMeetingEntity(meetingCreationRequest);
    UserEntity creator = userService.findByUUID(UUIDService.parseUUID(creatorUuid));
    baseMeeting.setCreator(creator);
    baseMeeting.setSuperId(superUUID);
    meetingEntities.add(baseMeeting);

    while(shouldCreateMeeting(baseMeeting) && !isUpdate) {
      MeetingEntity newMeeting = cloneMeetingEntity(baseMeeting);
      updateMeeting(newMeeting);
      meetingEntities.add(newMeeting);
      baseMeeting = newMeeting;
    }

    meetingRepository.saveAll(meetingEntities);
  }

  private void updateMeeting(MeetingEntity meetingEntity) {
    LocalDateTime startDate = meetingEntity.getDateFrom();
    LocalDateTime endDate = meetingEntity.getDateUntil();

    switch(meetingEntity.getRepeatable()) {
      case DAILY:
        meetingEntity.setDateFrom(startDate.plusDays(1));
        meetingEntity.setDateUntil(endDate.plusDays(1));
        break;
      case WEEKLY:
        meetingEntity.setDateFrom(startDate.plusWeeks(1));
        meetingEntity.setDateUntil(endDate.plusWeeks(1));
        break;
      case MONTHLY:
        meetingEntity.setDateFrom(startDate.plusMonths(1));
        meetingEntity.setDateUntil(endDate.plusMonths(1));
        break;
      default:
        throw new IllegalArgumentException("Invalid repeatable");
    }
  }

  private boolean shouldCreateMeeting(MeetingEntity meetingEntity) {
    if(meetingEntity.getRepeatable() == Repeat.NEVER) {
      return false;
    }
    return meetingEntity.getDateFrom().isBefore(endTime);

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
        .dateFrom(meetingEntity.getDateFrom())
        .dateUntil(meetingEntity.getDateUntil())
        .repeatable(meetingEntity.getRepeatable())
        .place(meetingEntity.getPlace())
        .creator(meetingEntity.getCreator())
        .build();
  }
}
