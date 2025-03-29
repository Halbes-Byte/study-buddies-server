package com.studybuddies.server.web.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.web.mapper.exceptions.EndDateAfterStartDateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class MeetingMapperTest {

  private MeetingMapper meetingMapper;

  // Mockito cant work with interfaces apparently
  @BeforeEach
  void setUp() {
    meetingMapper = Mappers.getMapper(MeetingMapper.class);
  }

  @Test
  void validate_shouldThrowException_whenStartDateIsAfterEndDate() {
    // given
    MeetingEntity meetingEntity = new MeetingEntity();
    meetingEntity.setDateFrom(LocalDateTime.of(2024, 2, 10, 10, 0));
    meetingEntity.setDateUntil(LocalDateTime.of(2024, 2, 9, 10, 0)); // End date before start date

    // when then
    EndDateAfterStartDateException exception = assertThrows(
        EndDateAfterStartDateException.class,
        () -> meetingMapper.validate(meetingEntity)
    );
    assertNotNull(exception);
  }

  @Test
  void validate_shouldNotThrowException_whenStartDateIsBeforeEndDate() {
    // Arrange
    MeetingEntity meetingEntity = new MeetingEntity();
    meetingEntity.setDateFrom(LocalDateTime.of(2024, 2, 9, 10, 0));
    meetingEntity.setDateUntil(LocalDateTime.of(2024, 2, 10, 10, 0));

    // Act & Assert
    assertDoesNotThrow(() -> meetingMapper.validate(meetingEntity));
  }
}
