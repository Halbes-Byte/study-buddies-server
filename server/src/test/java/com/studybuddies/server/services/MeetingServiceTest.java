package com.studybuddies.server.services;

import com.studybuddies.server.domain.UserEntity;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.persistance.MeetingRepository;
import com.studybuddies.server.services.exceptions.MeetingNotFoundException;
import com.studybuddies.server.web.dto.MeetingChangeRequest;
import com.studybuddies.server.web.dto.MeetingCreationRequest;
import com.studybuddies.server.web.mapper.MeetingMapper;
import com.studybuddies.server.web.mapper.exceptions.InvalidRepeatStringException;
import java.util.Optional;
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
  @Mock
  private UserService userService;

  @InjectMocks
  private MeetingService meetingService;

  @Test
  public void saveMeetingToDatabaseTest_invalidRepeatable_throwsException() {
    // given
    MeetingCreationRequest mockMeeting = new MeetingCreationRequest();
    mockMeeting.setTitle("Invalid Repeatable Meeting");
    mockMeeting.setDescription("Meeting with invalid repeatable value.");
    mockMeeting.setPlace("");
    mockMeeting.setRepeatable("invalid_value");
    mockMeeting.setDate_from("23-11-2020:15:30");
    mockMeeting.setDate_until("26-11-2020:15:30");

    UUID uuid = UUID.randomUUID();

    // when
    when(meetingMapper.meetingCreationRequestToMeetingEntity(mockMeeting))
        .thenThrow(new InvalidRepeatStringException("Invalid repeatable value"));
    when(userService.findByUUID(uuid)).thenReturn(any(UserEntity.class));

    // then
    assertThrows(InvalidRepeatStringException.class, () -> {
      meetingService.saveMeetingToDatabase(mockMeeting, uuid.toString());
    });

    // Verify that save was never called
    verify(meetingRepository, never()).save(any(MeetingEntity.class));
  }
  @Test
  public void saveMeetingToDatabase_success() {
    // given
    MeetingCreationRequest mockMeeting = new MeetingCreationRequest();
    mockMeeting.setTitle("Valid Meeting");
    mockMeeting.setDescription("");
    mockMeeting.setPlace("");
    mockMeeting.setRepeatable("NEVER");
    mockMeeting.setDate_from("23-11-2020:15:30");
    mockMeeting.setDate_until("26-11-2020:15:30");

    MeetingEntity mockMeetingEntity = new MeetingEntity();
    mockMeetingEntity.setId(1L);
    UUID uuid = UUID.randomUUID();

    when(meetingMapper.meetingCreationRequestToMeetingEntity(mockMeeting)).thenReturn(mockMeetingEntity);

    when(meetingRepository.save(any(MeetingEntity.class))).thenReturn(mockMeetingEntity);
    when(userService.findByUUID(uuid)).thenReturn(any(UserEntity.class));

    // when
    Long meetingId = meetingService.saveMeetingToDatabase(mockMeeting, uuid.toString());

    // then
    assertNotNull(meetingId);
    assertEquals(1L, meetingId);

    verify(meetingMapper).meetingCreationRequestToMeetingEntity(mockMeeting);
    verify(meetingRepository).save(mockMeetingEntity);
}

  @Test
  public void changeMeetingInDatabaseTest_meetingNotFound_throwsException() {
    // given
    UserEntity mockUser = new UserEntity();

    Long meetingId = 1L;
    MeetingChangeRequest mockChangeRequest = new MeetingChangeRequest();
    when(meetingRepository.findById(meetingId)).thenReturn(Optional.empty());

    // then
    assertThrows(MeetingNotFoundException.class, () -> {
      meetingService.changeMeetingInDatabase(meetingId, mockChangeRequest, mockUser.getUuid().toString());
    });

    verify(meetingRepository, never()).save(any(MeetingEntity.class));
  }

  @Test
  public void retrieveMeetingFromDatabaseTest_meetingNotFound_throwsException() {
    // given
    Long meetingId = 1L;
    when(meetingRepository.findById(meetingId)).thenReturn(Optional.empty());

    // then
    assertThrows(MeetingNotFoundException.class, () -> {
      meetingService.retrieveMeetingFromDatabase(meetingId);
    });
  }

  @Test
  public void deleteMeetingFromDatabaseTest_validId_deletesMeeting() {
    // given
    UserEntity mockUser = new UserEntity();
    Long meetingId = 1L;

    // when
    meetingService.deleteMeetingFromDatabase(meetingId, mockUser.getUuid().toString());

    // then
    verify(meetingRepository, times(1)).deleteById(meetingId);
  }
  @Test
  public void changeMeetingInDatabaseTest_updatesOnlyNonNullFields() {
    // given
    UserEntity mockUser = new UserEntity();
    Long meetingId = 1L;
    MeetingEntity existingMeeting = new MeetingEntity();
    existingMeeting.setTitle("Old Title");
    existingMeeting.setDescription("Old Description");
    existingMeeting.setPlace("Old Place");

    MeetingChangeRequest mockChangeRequest = new MeetingChangeRequest();
    mockChangeRequest.setTitle("New Title"); // Should be updated
    mockChangeRequest.setDescription(null);  // Should NOT be updated
    mockChangeRequest.setPlace(null);  // Should NOT be updated

    MeetingEntity changedMeeting = new MeetingEntity();
    changedMeeting.setTitle("New Title");
    changedMeeting.setDescription(null);
    changedMeeting.setPlace(null);

    when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(existingMeeting));
    when(meetingMapper.meetingChangeRequestToMeetingEntity(mockChangeRequest)).thenReturn(changedMeeting);

    // when
    meetingService.changeMeetingInDatabase(meetingId, mockChangeRequest, mockUser.getUuid().toString());

    // then
    assertEquals("New Title", existingMeeting.getTitle()); // Updated
    assertEquals("Old Description", existingMeeting.getDescription()); // Not updated
    assertEquals("Old Place", existingMeeting.getPlace()); // Not updated

    verify(meetingRepository, times(1)).save(existingMeeting);
  }
}
