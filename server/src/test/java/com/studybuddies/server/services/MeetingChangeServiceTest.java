package com.studybuddies.server.services;

import com.studybuddies.server.domain.ChangeType;
import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.domain.UserEntity;
import com.studybuddies.server.persistance.MeetingRepository;
import com.studybuddies.server.services.exceptions.InvalidUUIDException;
import com.studybuddies.server.services.exceptions.MeetingNotFoundException;
import com.studybuddies.server.services.meeting.MeetingChangeService;
import com.studybuddies.server.services.meeting.MeetingCreationService;
import com.studybuddies.server.services.user.UserService;
import com.studybuddies.server.web.dto.meeting.MeetingChangeRequest;
import com.studybuddies.server.web.dto.meeting.MeetingCreationRequest;
import com.studybuddies.server.web.mapper.MeetingMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeetingChangeServiceTest {

  @Mock
  private MeetingMapper meetingMapper;

  @Mock
  private MeetingRepository meetingRepository;

  @Mock
  private UserService userService;

  @Mock
  private MeetingCreationService meetingCreationService;

  @InjectMocks
  private MeetingChangeService meetingChangeService;

  @Test
  void update_OccurrenceChangeValidUser_UpdatesSuccessfully() {
    // given
    UUID meetingId = UUID.randomUUID();
    String clientUUID = UUID.randomUUID().toString();
    UUID clientUUIDParsed = UUID.randomUUID();

    MeetingChangeRequest request = new MeetingChangeRequest();
    request.setChangeType(ChangeType.OCCURRENCE);

    UserEntity creator = new UserEntity();
    creator.setUuid(clientUUIDParsed);

    MeetingEntity meeting = new MeetingEntity();
    meeting.setCreator(creator);
    meeting.setSuperId(UUID.randomUUID());

    MeetingEntity mappedMeeting = new MeetingEntity();
    MeetingCreationRequest creationRequest = new MeetingCreationRequest();

    try (MockedStatic<UUIDService> uuidServiceMock = mockStatic(UUIDService.class);
        MockedStatic<MergingService> mergingServiceMock = mockStatic(MergingService.class)) {

      uuidServiceMock.when(() -> UUIDService.parseUUID(clientUUID)).thenReturn(clientUUIDParsed);
      when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting));
      when(userService.existsByUUID(clientUUIDParsed)).thenReturn(true);
      when(meetingMapper.of(request)).thenReturn(mappedMeeting);
      when(meetingMapper.meetingEntityToMeetingCreationRequest(meeting)).thenReturn(creationRequest);

      // when
      meetingChangeService.update(meetingId, request, clientUUID);

      // then
      verify(meetingRepository).deleteAll(List.of(meeting));
      verify(meetingCreationService).createMeetingInstances(creationRequest, clientUUIDParsed.toString(), meeting.getSuperId(), true);
      mergingServiceMock.verify(() -> MergingService.mergeObjects(mappedMeeting, meeting));
    }
  }

  @Test
  void update_SeriesChangeValidUser_UpdatesAllMeetings() {
    // given
    UUID meetingId = UUID.randomUUID();
    String clientUUID = UUID.randomUUID().toString();
    UUID clientUUIDParsed = UUID.randomUUID();
    UUID superId = UUID.randomUUID();

    MeetingChangeRequest request = new MeetingChangeRequest();
    request.setChangeType(ChangeType.SERIES);

    UserEntity creator = new UserEntity();
    creator.setUuid(clientUUIDParsed);

    MeetingEntity meeting1 = new MeetingEntity();
    meeting1.setCreator(creator);
    meeting1.setSuperId(superId);

    MeetingEntity meeting2 = new MeetingEntity();
    meeting2.setCreator(creator);
    meeting2.setSuperId(superId);

    List<MeetingEntity> seriesMeetings = List.of(meeting1, meeting2);

    MeetingEntity mappedMeeting = new MeetingEntity();
    MeetingCreationRequest creationRequest = new MeetingCreationRequest();

    try (MockedStatic<UUIDService> uuidServiceMock = mockStatic(UUIDService.class);
        MockedStatic<MergingService> mergingServiceMock = mockStatic(MergingService.class)) {

      uuidServiceMock.when(() -> UUIDService.parseUUID(clientUUID)).thenReturn(clientUUIDParsed);
      when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting1));
      when(meetingRepository.findBySuperId(superId)).thenReturn(seriesMeetings);
      when(userService.existsByUUID(clientUUIDParsed)).thenReturn(true);
      when(meetingMapper.of(request)).thenReturn(mappedMeeting);
      when(meetingMapper.meetingEntityToMeetingCreationRequest(meeting1)).thenReturn(creationRequest);

      // when
      meetingChangeService.update(meetingId, request, clientUUID);

      // then
      verify(meetingRepository).deleteAll(seriesMeetings);
      verify(meetingCreationService).createMeetingInstances(creationRequest, clientUUIDParsed.toString(), superId, false);
      mergingServiceMock.verify(() -> MergingService.mergeObjects(mappedMeeting, meeting1));
    }
  }

  @Test
  void update_UserNotExists_ThrowsInvalidUUIDException() {
    // given
    UUID meetingId = UUID.randomUUID();
    String clientUUID = UUID.randomUUID().toString();
    UUID clientUUIDParsed = UUID.randomUUID();

    MeetingChangeRequest request = new MeetingChangeRequest();
    request.setChangeType(ChangeType.OCCURRENCE);

    UserEntity creator = new UserEntity();
    creator.setUuid(clientUUIDParsed);

    MeetingEntity meeting = new MeetingEntity();
    meeting.setCreator(creator);

    try (MockedStatic<UUIDService> uuidServiceMock = mockStatic(UUIDService.class)) {
      uuidServiceMock.when(() -> UUIDService.parseUUID(clientUUID)).thenReturn(clientUUIDParsed);
      when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting));
      when(userService.existsByUUID(clientUUIDParsed)).thenReturn(false);

      // when & then
      assertThrows(InvalidUUIDException.class, () -> meetingChangeService.update(meetingId, request, clientUUID));
    }
  }

  @Test
  void update_NotMeetingOwner_ThrowsMeetingNotFoundException() {
    // given
    UUID meetingId = UUID.randomUUID();
    String clientUUID = UUID.randomUUID().toString();
    UUID clientUUIDParsed = UUID.randomUUID();
    UUID differentOwnerUUID = UUID.randomUUID();

    MeetingChangeRequest request = new MeetingChangeRequest();
    request.setChangeType(ChangeType.OCCURRENCE);

    UserEntity creator = new UserEntity();
    creator.setUuid(differentOwnerUUID);

    MeetingEntity meeting = new MeetingEntity();
    meeting.setCreator(creator);

    try (MockedStatic<UUIDService> uuidServiceMock = mockStatic(UUIDService.class)) {
      uuidServiceMock.when(() -> UUIDService.parseUUID(clientUUID)).thenReturn(clientUUIDParsed);
      when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting));
      when(userService.existsByUUID(clientUUIDParsed)).thenReturn(true);

      // when & then
      assertThrows(MeetingNotFoundException.class, () -> meetingChangeService.update(meetingId, request, clientUUID));
    }
  }

  @Test
  void update_MeetingNotFound_ThrowsMeetingNotFoundException() {
    // given
    UUID meetingId = UUID.randomUUID();
    String clientUUID = UUID.randomUUID().toString();
    MeetingChangeRequest request = new MeetingChangeRequest();
    request.setChangeType(ChangeType.OCCURRENCE);

    when(meetingRepository.findById(meetingId)).thenReturn(Optional.empty());

    // when & then
    assertThrows(MeetingNotFoundException.class, () -> meetingChangeService.update(meetingId, request, clientUUID));
  }

  @Test
  void update_SeriesButNoMeetingsFound_ThrowsMeetingNotFoundException() {
    // given
    UUID meetingId = UUID.randomUUID();
    String clientUUID = UUID.randomUUID().toString();
    UUID clientUUIDParsed = UUID.randomUUID();
    UUID superId = UUID.randomUUID();

    MeetingChangeRequest request = new MeetingChangeRequest();
    request.setChangeType(ChangeType.SERIES);

    UserEntity creator = new UserEntity();
    creator.setUuid(clientUUIDParsed);

    MeetingEntity meeting = new MeetingEntity();
    meeting.setCreator(creator);
    meeting.setSuperId(superId);

    try (MockedStatic<UUIDService> uuidServiceMock = mockStatic(UUIDService.class)) {
      uuidServiceMock.when(() -> UUIDService.parseUUID(clientUUID)).thenReturn(clientUUIDParsed);
      when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting));
      when(meetingRepository.findBySuperId(superId)).thenReturn(List.of());

      // when & then
      assertThrows(MeetingNotFoundException.class, () -> meetingChangeService.update(meetingId, request, clientUUID));
    }
  }
}
