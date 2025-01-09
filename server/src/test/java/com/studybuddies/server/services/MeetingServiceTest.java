package com.studybuddies.server.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.domain.Repeat;
import com.studybuddies.server.persistance.MeetingRepository;
import com.studybuddies.server.web.dto.MeetingCreationRequest;
import com.studybuddies.server.web.mapper.MeetingMapper;
import com.studybuddies.server.web.mapper.exceptions.InvalidRepeatStringException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MeetingServiceTest {

  @Mock
  private MeetingMapper meetingMapper;
  @Mock
  private MeetingRepository meetingRepository;

  @InjectMocks
  private MeetingService meetingService;

  @Test
  public void saveMeetingToDatabaseTest_invalidRepeatable_throwsException() {
    // given
    MeetingCreationRequest mockMeeting = new MeetingCreationRequest();
    mockMeeting.setTitle("Invalid Repeatable Meeting");
    mockMeeting.setDescription("Meeting with invalid repeatable value.");
    mockMeeting.setLinks("");
    mockMeeting.setPlace("");
    mockMeeting.setRepeatable("invalid_value");
    mockMeeting.setDate_from("23-11-2020:15:30");
    mockMeeting.setDate_until("26-11-2020:15:30");

    // when
    when(meetingMapper.MeetingCreationRequestToMeetingEntity(mockMeeting))
        .thenThrow(new InvalidRepeatStringException("Invalid repeatable value"));

    // then
    assertThrows(InvalidRepeatStringException.class, () -> {
      meetingService.saveMeetingToDatabase(mockMeeting);
    });

    // Verify that save was never called
    verify(meetingRepository, never()).save(any(MeetingEntity.class));
  }
}
