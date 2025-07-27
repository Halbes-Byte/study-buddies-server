package com.studybuddies.server.services;

import com.studybuddies.server.domain.Filter;
import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.persistance.MeetingRepository;
import com.studybuddies.server.services.exceptions.ModuleNotFoundException;
import com.studybuddies.server.services.meeting.MeetingChangeService;
import com.studybuddies.server.services.meeting.MeetingCreationService;
import com.studybuddies.server.services.meeting.MeetingService;
import com.studybuddies.server.services.meeting.MeetingSpecificationService;
import com.studybuddies.server.web.dto.meeting.MeetingChangeRequest;
import com.studybuddies.server.web.dto.meeting.MeetingCreationRequest;
import com.studybuddies.server.web.dto.meeting.MeetingResponse;
import com.studybuddies.server.web.mapper.MeetingMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeetingServiceTest {

  @Mock
  private MeetingMapper meetingMapper;

  @Mock
  private MeetingRepository meetingRepository;

  @Mock
  private MeetingCreationService meetingCreationService;

  @Mock
  private MeetingChangeService meetingChangeService;

  @Mock
  private Specification<MeetingEntity> specification;

  @InjectMocks
  private MeetingService meetingService;

  @Test
  void get_NoFilter_ReturnsAllMeetingsForParticipant() {
    // given
    String clientUUID = UUID.randomUUID().toString();
    UUID clientUUIDParsed = UUID.randomUUID();

    MeetingEntity meeting1 = new MeetingEntity();
    MeetingEntity meeting2 = new MeetingEntity();
    List<MeetingEntity> meetings = List.of(meeting1, meeting2);

    MeetingResponse response1 = new MeetingResponse();
    MeetingResponse response2 = new MeetingResponse();

    try (MockedStatic<UUIDService> uuidServiceMock = mockStatic(UUIDService.class);
        MockedStatic<MeetingSpecificationService> specServiceMock = mockStatic(MeetingSpecificationService.class)) {

      uuidServiceMock.when(() -> UUIDService.parseUUID(clientUUID)).thenReturn(clientUUIDParsed);
      specServiceMock.when(() -> MeetingSpecificationService.getSpecParticipant(clientUUIDParsed)).thenReturn(specification);
      when(meetingRepository.findAll(specification)).thenReturn(meetings);
      when(meetingMapper.of(meeting1)).thenReturn(response1);
      when(meetingMapper.of(meeting2)).thenReturn(response2);

      // when
      List<MeetingResponse> result = meetingService.get(clientUUID, null);

      // then
      assertEquals(2, result.size());
      assertEquals(response1, result.get(0));
      assertEquals(response2, result.get(1));
      verify(meetingRepository).findAll(specification);
    }
  }

  @Test
  void get_WithModuleFilter_ReturnsFilteredMeetings() {
    // given
    String clientUUID = UUID.randomUUID().toString();
    UUID clientUUIDParsed = UUID.randomUUID();
    String module = "math";

    Filter filter = mock(Filter.class);
    Map<String, String> filters = new HashMap<>();
    filters.put("module", module);
    when(filter.getFilters()).thenReturn((HashMap<String, String>) filters);

    MeetingEntity meeting = new MeetingEntity();
    List<MeetingEntity> meetings = List.of(meeting);
    MeetingResponse response = new MeetingResponse();

    try (MockedStatic<UUIDService> uuidServiceMock = mockStatic(UUIDService.class);
        MockedStatic<MeetingSpecificationService> specServiceMock = mockStatic(MeetingSpecificationService.class)) {

      uuidServiceMock.when(() -> UUIDService.parseUUID(clientUUID)).thenReturn(clientUUIDParsed);
      specServiceMock.when(() -> MeetingSpecificationService.getSpecRelevantMeetings(clientUUIDParsed, "MATH")).thenReturn(specification);
      when(meetingRepository.findAll(specification)).thenReturn(meetings);
      when(meetingMapper.of(meeting)).thenReturn(response);

      // when
      List<MeetingResponse> result = meetingService.get(clientUUID, filter);

      // then
      assertEquals(1, result.size());
      assertEquals(response, result.get(0));
      verify(meetingRepository).findAll(specification);
    }
  }

  @Test
  void get_WithFilterButNoModule_ThrowsModuleNotFoundException() {
    // given
    String clientUUID = UUID.randomUUID().toString();
    Filter filter = mock(Filter.class);
    Map<String, String> filters = new HashMap<>();
    when(filter.getFilters()).thenReturn((HashMap<String, String>) filters);

    // when & then
    assertThrows(ModuleNotFoundException.class, () -> meetingService.get(clientUUID, filter));
  }

  @Test
  void create_ValidRequest_CallsCreationService() {
    // given
    MeetingCreationRequest request = new MeetingCreationRequest();
    String creatorUuid = UUID.randomUUID().toString();

    // when
    meetingService.create(request, creatorUuid);

    // then
    verify(meetingCreationService).createMeetingInstances(request, creatorUuid, null, false);
  }

  @Test
  void update_ValidRequest_CallsChangeService() {
    // given
    String meetingId = UUID.randomUUID().toString();
    UUID meetingUUID = UUID.randomUUID();
    MeetingChangeRequest request = new MeetingChangeRequest();
    String clientUUID = UUID.randomUUID().toString();

    try (MockedStatic<UUIDService> uuidServiceMock = mockStatic(UUIDService.class)) {
      uuidServiceMock.when(() -> UUIDService.parseUUID(meetingId)).thenReturn(meetingUUID);

      // when
      meetingService.update(meetingId, request, clientUUID);

      // then
      verify(meetingChangeService).update(meetingUUID, request, clientUUID);
    }
  }

  @Test
  void delete_AlwaysThrowsNotImplementedException() {
    // given
    String targetUUID = UUID.randomUUID().toString();
    String clientUUID = UUID.randomUUID().toString();

    // when & then
    assertThrows(NotImplementedException.class, () -> meetingService.delete(targetUUID, clientUUID));
  }

  @Test
  void findMeetingByUUID_MeetingExists_ReturnsMeeting() {
    // given
    String uuid = UUID.randomUUID().toString();
    UUID meetingUUID = UUID.randomUUID();
    MeetingEntity meeting = new MeetingEntity();

    try (MockedStatic<UUIDService> uuidServiceMock = mockStatic(UUIDService.class)) {
      uuidServiceMock.when(() -> UUIDService.parseUUID(uuid)).thenReturn(meetingUUID);
      when(meetingRepository.findById(meetingUUID)).thenReturn(Optional.of(meeting));

      // when
      MeetingEntity result = meetingService.findMeetingByUUID(uuid);

      // then
      assertEquals(meeting, result);
    }
  }

  @Test
  void findMeetingByUUID_MeetingNotExists_ReturnsNull() {
    // given
    String uuid = UUID.randomUUID().toString();
    UUID meetingUUID = UUID.randomUUID();

    try (MockedStatic<UUIDService> uuidServiceMock = mockStatic(UUIDService.class)) {
      uuidServiceMock.when(() -> UUIDService.parseUUID(uuid)).thenReturn(meetingUUID);
      when(meetingRepository.findById(meetingUUID)).thenReturn(Optional.empty());

      // when
      MeetingEntity result = meetingService.findMeetingByUUID(uuid);

      // then
      assertNull(result);
    }
  }

  @Test
  void findMeetingsBySuperID_ValidSuperID_ReturnsMeetings() {
    // given
    String superID = UUID.randomUUID().toString();
    UUID superUUID = UUID.randomUUID();
    MeetingEntity meeting1 = new MeetingEntity();
    MeetingEntity meeting2 = new MeetingEntity();
    List<MeetingEntity> meetings = List.of(meeting1, meeting2);

    try (MockedStatic<UUIDService> uuidServiceMock = mockStatic(UUIDService.class)) {
      uuidServiceMock.when(() -> UUIDService.parseUUID(superID)).thenReturn(superUUID);
      when(meetingRepository.findBySuperId(superUUID)).thenReturn(meetings);

      // when
      List<MeetingEntity> result = meetingService.findMeetingsBySuperID(superID);

      // then
      assertEquals(2, result.size());
      assertEquals(meeting1, result.get(0));
      assertEquals(meeting2, result.get(1));
    }
  }
}
