package com.studybuddies.server.services;

import com.studybuddies.server.domain.ChangeType;
import com.studybuddies.server.domain.UserEntity;
import com.studybuddies.server.services.meeting.MeetingChangeService;
import com.studybuddies.server.services.meeting.MeetingCreationService;
import com.studybuddies.server.services.meeting.MeetingService;
import com.studybuddies.server.services.user.UserService;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
class MeetingServiceTest {

  @Mock
  private MeetingMapper meetingMapper;
  @Mock
  private MeetingRepository meetingRepository;
  @Mock
  private UserService userService;
  @Mock
  private MeetingCreationService meetingCreationService;
  @Mock
  private MeetingChangeService meetingChangeService;

  @InjectMocks
  private MeetingService meetingService;

  @Test
  void createMeetingsTest_invalidRepeatable_throwsException() {
    // given
    MeetingCreationRequest mockMeeting = new MeetingCreationRequest();
    mockMeeting.setTitle("Invalid Repeatable Meeting");
    mockMeeting.setDescription("Meeting with invalid repeatable value.");
    mockMeeting.setPlace("");
    mockMeeting.setRepeatable("invalid_value");
    mockMeeting.setDateFrom("23-11-2020:15:30");
    mockMeeting.setDateUntil("26-11-2020:15:30");

    UUID uuid = UUID.randomUUID();

    // when
    when(meetingMapper.meetingCreationRequestToMeetingEntity(mockMeeting))
        .thenThrow(new InvalidRepeatStringException("Invalid repeatable value"));

    // then
    assertThrows(InvalidRepeatStringException.class, () -> {
      meetingService.create(mockMeeting, uuid.toString());
    });

    // Verify that save was never called
    verify(meetingRepository, never()).save(any(MeetingEntity.class));
  }

  @Test
  void createMeetings_success() {
    // given
    MeetingCreationRequest mockMeeting = new MeetingCreationRequest();
    mockMeeting.setTitle("Valid Meeting");
    mockMeeting.setDescription("");
    mockMeeting.setPlace("");
    mockMeeting.setRepeatable("NEVER");
    mockMeeting.setDateFrom("23-11-2020:15:30");
    mockMeeting.setDateUntil("26-11-2020:15:30");

    MeetingEntity mockMeetingEntity = new MeetingEntity();
    // mockMeetingEntity.setId(1L);
    UUID uuid = UUID.randomUUID();

    when(meetingMapper.meetingCreationRequestToMeetingEntity(mockMeeting)).thenReturn(
        mockMeetingEntity);

    when(meetingRepository.save(any(MeetingEntity.class))).thenReturn(mockMeetingEntity);
    when(userService.findByUUID(uuid)).thenReturn(any(UserEntity.class));

    // when
    meetingService.create(mockMeeting, uuid.toString());

    // then

    verify(meetingMapper).meetingCreationRequestToMeetingEntity(mockMeeting);
    verify(meetingRepository).save(mockMeetingEntity);
  }

  @Test
  void updateMeetingInDatabaseTest_NotFound_throwsException() {
    // given
    UserEntity mockUser = new UserEntity();
    mockUser.setUuid(UUID.randomUUID());

    UUID meetingId = UUID.randomUUID();
    MeetingChangeRequest mockChangeRequest = new MeetingChangeRequest();
    mockChangeRequest.setChangeType(ChangeType.OCCURRENCE);
    when(meetingRepository.findById(meetingId)).thenReturn(Optional.empty());

    // then
    assertThrows(MeetingNotFoundException.class, () -> {
      meetingService.update(meetingId.toString(), mockChangeRequest, mockUser.getUuid().toString());
    });

    verify(meetingRepository, never()).save(any(MeetingEntity.class));
  }

  @Test
  void retrieveMeetingFromDatabaseTest_meetingNotFound_throwsException() {
    // given
    UUID meetingId = UUID.randomUUID();
    when(meetingRepository.findById(meetingId)).thenReturn(Optional.empty());

    // then
    assertThrows(MeetingNotFoundException.class, () -> {
      meetingService.get(meetingId.toString());
    });
  }

  @Test
  void deleteMeetingFromDatabaseTest_validId_deletesMeeting() {
    // given
    UUID meetingId = UUID.randomUUID();
    UserEntity mockUser = new UserEntity();
    mockUser.setUuid(UUID.randomUUID());

    MeetingEntity mockMeetingEntity = new MeetingEntity();
    mockMeetingEntity.setId(meetingId);
    mockMeetingEntity.setCreator(mockUser);
    when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(mockMeetingEntity));

    // when
    meetingService.delete(meetingId.toString(), mockUser.getUuid().toString());

    // then
    verify(meetingRepository, times(1)).deleteById(meetingId);
  }

  @Test
  void updateTest_updatesOnlyNonNullFields() {
    // given
    UserEntity mockUser = new UserEntity();
    mockUser.setUuid(UUID.randomUUID());
    UUID meetingId = UUID.randomUUID();
    MeetingEntity existingMeeting = new MeetingEntity();
    existingMeeting.setTitle("Old Title");
    existingMeeting.setDescription("Old Description");
    existingMeeting.setPlace("Old Place");
    existingMeeting.setCreator(mockUser);

    MeetingChangeRequest mockChangeRequest = new MeetingChangeRequest();
    mockChangeRequest.setTitle("New Title"); // Should be updated
    mockChangeRequest.setDescription(null);  // Should NOT be updated
    mockChangeRequest.setPlace(null);  // Should NOT be updated
    mockChangeRequest.setChangeType(ChangeType.OCCURRENCE);

    MeetingEntity changedMeeting = new MeetingEntity();
    changedMeeting.setTitle("New Title");
    changedMeeting.setDescription(null);
    changedMeeting.setPlace(null);

    when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(existingMeeting));
    when(meetingMapper.meetingChangeRequestToMeetingEntity(mockChangeRequest)).thenReturn(
        changedMeeting);

    // when
    meetingService.update(meetingId.toString(), mockChangeRequest, mockUser.getUuid().toString());

    // then
    assertEquals("New Title", existingMeeting.getTitle()); // Updated
    assertEquals("Old Description", existingMeeting.getDescription()); // Not updated
    assertEquals("Old Place", existingMeeting.getPlace()); // Not updated

    verify(meetingRepository, times(1)).save(existingMeeting);
  }
}
