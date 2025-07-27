package com.studybuddies.server.services;

import com.studybuddies.server.domain.Filter;
import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.domain.StudyGroupEntity;
import com.studybuddies.server.domain.UserEntity;
import com.studybuddies.server.persistance.StudyGroupRepository;
import com.studybuddies.server.services.exceptions.InvalidUUIDException;
import com.studybuddies.server.services.meeting.MeetingService;
import com.studybuddies.server.services.user.UserService;
import com.studybuddies.server.web.dto.studygroup.StudyGroupJoinRequest;
import com.studybuddies.server.web.dto.studygroup.StudyGroupResponse;
import com.studybuddies.server.web.mapper.StudyGroupMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudyGroupServiceTest {

  @Mock
  private StudyGroupRepository studyGroupRepository;

  @Mock
  private MeetingService meetingService;

  @Mock
  private UserService userService;

  @Mock
  private StudyGroupMapper studyGroupMapper;

  @InjectMocks
  private StudyGroupService studyGroupService;

  @Test
  void get_shouldReturnMappedStudyGroupResponses() {
    // given
    String id = "550e8400-e29b-41d4-a716-446655440000";
    UUID uuid = UUID.fromString(id);
    Filter filter = Filter.of(null, "test", "value");

    StudyGroupEntity studyGroup1 = new StudyGroupEntity();
    StudyGroupEntity studyGroup2 = new StudyGroupEntity();
    List<StudyGroupEntity> studyGroups = Arrays.asList(studyGroup1, studyGroup2);

    StudyGroupResponse response1 = new StudyGroupResponse();
    response1.setMeetingId("meeting1");
    StudyGroupResponse response2 = new StudyGroupResponse();
    response2.setMeetingId("meeting2");

    try (MockedStatic<UUIDService> mockedUUIDService = mockStatic(UUIDService.class)) {
      mockedUUIDService.when(() -> UUIDService.parseUUID(id)).thenReturn(uuid);
      when(studyGroupRepository.findByUserIdOrMeetingId(uuid)).thenReturn(Optional.of(studyGroups));
      when(studyGroupMapper.of(studyGroup1)).thenReturn(response1);
      when(studyGroupMapper.of(studyGroup2)).thenReturn(response2);

      // when
      List<StudyGroupResponse> result = studyGroupService.get(id, filter);

      // then
      assertEquals(2, result.size());
      assertEquals("meeting1", result.get(0).getMeetingId());
      assertEquals("meeting2", result.get(1).getMeetingId());
      verify(studyGroupRepository).findByUserIdOrMeetingId(uuid);
      verify(studyGroupMapper).of(studyGroup1);
      verify(studyGroupMapper).of(studyGroup2);
    }
  }

  @Test
  void get_shouldThrowExceptionWhenNoStudyGroupsFound() {
    // given
    String id = "550e8400-e29b-41d4-a716-446655440000";
    UUID uuid = UUID.fromString(id);
    Filter filter = Filter.of(null, "test", "value");

    try (MockedStatic<UUIDService> mockedUUIDService = mockStatic(UUIDService.class)) {
      mockedUUIDService.when(() -> UUIDService.parseUUID(id)).thenReturn(uuid);
      when(studyGroupRepository.findByUserIdOrMeetingId(uuid)).thenReturn(Optional.empty());

      // when & then
      assertThrows(InvalidUUIDException.class, () -> {
        studyGroupService.get(id, filter);
      });

      verify(studyGroupRepository).findByUserIdOrMeetingId(uuid);
      verifyNoInteractions(studyGroupMapper);
    }
  }

  @Test
  void create_shouldJoinSingleMeetingWhenMeetingIdProvided() {
    // given
    String clientUuid = "550e8400-e29b-41d4-a716-446655440000";
    UUID userUuid = UUID.fromString(clientUuid);
    String meetingId = "550e8400-e29b-41d4-a716-446655440001";

    StudyGroupJoinRequest request = new StudyGroupJoinRequest();
    request.meetingId = meetingId;

    UserEntity userEntity = new UserEntity();
    userEntity.setUuid(userUuid);

    MeetingEntity meetingEntity = new MeetingEntity();
    meetingEntity.setId(UUID.fromString(meetingId));

    try (MockedStatic<UUIDService> mockedUUIDService = mockStatic(UUIDService.class)) {
      mockedUUIDService.when(() -> UUIDService.parseUUID(clientUuid)).thenReturn(userUuid);
      when(userService.findByUUID(userUuid)).thenReturn(userEntity);
      when(meetingService.findMeetingByUUID(meetingId)).thenReturn(meetingEntity);

      // when
      studyGroupService.create(request, clientUuid);

      // then
      verify(userService).findByUUID(userUuid);
      verify(meetingService).findMeetingByUUID(meetingId);
      verify(studyGroupRepository).save(any(StudyGroupEntity.class));
      verify(meetingService, never()).findMeetingsBySuperID(any());
    }
  }

  @Test
  void create_shouldJoinMultipleMeetingsWhenSuperMeetingIdProvided() {
    // given
    String clientUuid = "550e8400-e29b-41d4-a716-446655440000";
    UUID userUuid = UUID.fromString(clientUuid);
    String superMeetingId = "550e8400-e29b-41d4-a716-446655440001";

    StudyGroupJoinRequest request = new StudyGroupJoinRequest();
    request.superMeetingId = superMeetingId;

    UserEntity userEntity = new UserEntity();
    userEntity.setUuid(userUuid);

    MeetingEntity meeting1 = new MeetingEntity();
    meeting1.setId(UUID.randomUUID());
    MeetingEntity meeting2 = new MeetingEntity();
    meeting2.setId(UUID.randomUUID());
    List<MeetingEntity> meetings = Arrays.asList(meeting1, meeting2);

    try (MockedStatic<UUIDService> mockedUUIDService = mockStatic(UUIDService.class)) {
      mockedUUIDService.when(() -> UUIDService.parseUUID(clientUuid)).thenReturn(userUuid);
      when(userService.findByUUID(userUuid)).thenReturn(userEntity);
      when(meetingService.findMeetingsBySuperID(superMeetingId)).thenReturn(meetings);

      // when
      studyGroupService.create(request, clientUuid);

      // then
      verify(userService).findByUUID(userUuid);
      verify(meetingService).findMeetingsBySuperID(superMeetingId);
      verify(studyGroupRepository, times(2)).save(any(StudyGroupEntity.class));
      verify(meetingService, never()).findMeetingByUUID(any());
    }
  }

  @Test
  void create_shouldDoNothingWhenNoMeetingIdsProvided() {
    // given
    String clientUuid = "550e8400-e29b-41d4-a716-446655440000";
    UUID userUuid = UUID.fromString(clientUuid);

    StudyGroupJoinRequest request = new StudyGroupJoinRequest();
    // Both meetingId and superMeetingId are null

    UserEntity userEntity = new UserEntity();
    userEntity.setUuid(userUuid);

    try (MockedStatic<UUIDService> mockedUUIDService = mockStatic(UUIDService.class)) {
      mockedUUIDService.when(() -> UUIDService.parseUUID(clientUuid)).thenReturn(userUuid);
      when(userService.findByUUID(userUuid)).thenReturn(userEntity);

      // when
      studyGroupService.create(request, clientUuid);

      // then
      verify(userService).findByUUID(userUuid);
      verifyNoInteractions(meetingService);
      verify(studyGroupRepository, never()).save(any());
    }
  }

  @Test
  void create_shouldHandleEmptyMeetingIds() {
    // given
    String clientUuid = "550e8400-e29b-41d4-a716-446655440000";
    UUID userUuid = UUID.fromString(clientUuid);

    StudyGroupJoinRequest request = new StudyGroupJoinRequest();
    request.meetingId = "";
    request.superMeetingId = "";

    UserEntity userEntity = new UserEntity();
    userEntity.setUuid(userUuid);

    try (MockedStatic<UUIDService> mockedUUIDService = mockStatic(UUIDService.class)) {
      mockedUUIDService.when(() -> UUIDService.parseUUID(clientUuid)).thenReturn(userUuid);
      when(userService.findByUUID(userUuid)).thenReturn(userEntity);

      // when
      studyGroupService.create(request, clientUuid);

      // then
      verify(userService).findByUUID(userUuid);
      verifyNoInteractions(meetingService);
      verify(studyGroupRepository, never()).save(any());
    }
  }

  @Test
  void delete_shouldCallBothLeaveMethods() {
    // given
    String targetUUID = "550e8400-e29b-41d4-a716-446655440000";
    String clientUUID = "550e8400-e29b-41d4-a716-446655440001";
    UUID targetUuid = UUID.fromString(targetUUID);
    UUID clientUuid = UUID.fromString(clientUUID);

    try (MockedStatic<UUIDService> mockedUUIDService = mockStatic(UUIDService.class)) {
      mockedUUIDService.when(() -> UUIDService.parseUUID(clientUUID)).thenReturn(clientUuid);
      mockedUUIDService.when(() -> UUIDService.parseUUID(targetUUID)).thenReturn(targetUuid);

      // when
      studyGroupService.delete(targetUUID, clientUUID);

      // then
      verify(studyGroupRepository).deleteByUserIdAndMeetingId(clientUuid, targetUuid);
      verify(studyGroupRepository).deleteByUserIdAndSuperMeetingId(clientUuid, targetUuid);
    }
  }

  @Test
  void joinMeeting_shouldCreateAndSaveStudyGroupEntity() {
    // given
    UUID userId = UUID.randomUUID();
    UUID meetingId = UUID.randomUUID();

    UserEntity userEntity = new UserEntity();
    userEntity.setUuid(userId);

    MeetingEntity meetingEntity = new MeetingEntity();
    meetingEntity.setId(meetingId);

    // when
    // Using reflection to test private method
    try {
      java.lang.reflect.Method method = StudyGroupService.class.getDeclaredMethod("joinMeeting", UserEntity.class, MeetingEntity.class);
      method.setAccessible(true);
      method.invoke(studyGroupService, userEntity, meetingEntity);

      // then
      verify(studyGroupRepository).save(any(StudyGroupEntity.class));
    } catch (Exception e) {
      fail("Failed to test private method: " + e.getMessage());
    }
  }

  @Test
  void leaveMeeting_shouldCallRepositoryDeleteMethod() {
    // given
    String userUUID = "550e8400-e29b-41d4-a716-446655440000";
    String meetingUUID = "550e8400-e29b-41d4-a716-446655440001";
    UUID userUuid = UUID.fromString(userUUID);
    UUID meetingUuid = UUID.fromString(meetingUUID);

    try (MockedStatic<UUIDService> mockedUUIDService = mockStatic(UUIDService.class)) {
      mockedUUIDService.when(() -> UUIDService.parseUUID(userUUID)).thenReturn(userUuid);
      mockedUUIDService.when(() -> UUIDService.parseUUID(meetingUUID)).thenReturn(meetingUuid);

      // when
      // Using reflection to test private method
      try {
        java.lang.reflect.Method method = StudyGroupService.class.getDeclaredMethod("leaveMeeting", String.class, String.class);
        method.setAccessible(true);
        method.invoke(studyGroupService, userUUID, meetingUUID);

        // then
        verify(studyGroupRepository).deleteByUserIdAndMeetingId(userUuid, meetingUuid);
      } catch (Exception e) {
        fail("Failed to test private method: " + e.getMessage());
      }
    }
  }

  @Test
  void leaveSuperMeeting_shouldCallRepositoryDeleteMethod() {
    // given
    String userUUID = "550e8400-e29b-41d4-a716-446655440000";
    String superMeetingUUID = "550e8400-e29b-41d4-a716-446655440001";
    UUID userUuid = UUID.fromString(userUUID);
    UUID superMeetingUuid = UUID.fromString(superMeetingUUID);

    try (MockedStatic<UUIDService> mockedUUIDService = mockStatic(UUIDService.class)) {
      mockedUUIDService.when(() -> UUIDService.parseUUID(userUUID)).thenReturn(userUuid);
      mockedUUIDService.when(() -> UUIDService.parseUUID(superMeetingUUID)).thenReturn(superMeetingUuid);

      // when
      // Using reflection to test private method
      try {
        java.lang.reflect.Method method = StudyGroupService.class.getDeclaredMethod("leaveSuperMeeting", String.class, String.class);
        method.setAccessible(true);
        method.invoke(studyGroupService, userUUID, superMeetingUUID);

        // then
        verify(studyGroupRepository).deleteByUserIdAndSuperMeetingId(userUuid, superMeetingUuid);
      } catch (Exception e) {
        fail("Failed to test private method: " + e.getMessage());
      }
    }
  }
}
