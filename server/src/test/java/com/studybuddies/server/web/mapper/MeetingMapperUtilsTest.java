package com.studybuddies.server.web.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.studybuddies.server.web.mapper.exceptions.DateFormatException;
import java.time.LocalDateTime;

import com.studybuddies.server.domain.Repeat;
import com.studybuddies.server.web.mapper.exceptions.InvalidRepeatStringException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MeetingMapperUtilsTest {

  @InjectMocks
  private MeetingMapperUtils meetingMapperUtils;

  @Test
  void stringToLocalDate_shouldConvertValidStringToLocalDateTime() {
    // Given
    String validDate = "10-02-2024:10:00";

    // When
    LocalDateTime result = meetingMapperUtils.stringToLocalDate(validDate);

    // Then
    assertNotNull(result);
    assertEquals(LocalDateTime.of(2024, 2, 10, 10, 0), result);
  }

  @Test
  void stringToLocalDate_shouldThrowException_whenInvalidFormat() {
    // Given
    String invalidDate = "invalid-date";

    // When & Then
    assertThrows(DateFormatException.class, () -> meetingMapperUtils.stringToLocalDate(invalidDate));
  }

  @Test
  void changeStringToLocalDate_shouldReturnNull_whenInputIsNullOrEmpty() {
    // Given
    String emptyDate = "";
    String nullDate = null;

    // When & Then
    assertNull(meetingMapperUtils.changeStringToLocalDate(emptyDate));
    assertNull(meetingMapperUtils.changeStringToLocalDate(nullDate));
  }

  @Test
  void changeStringToLocalDate_shouldConvertValidStringToLocalDateTime() {
    // Given
    String validDate = "10-02-2024:10:00";

    // When
    LocalDateTime result = meetingMapperUtils.changeStringToLocalDate(validDate);

    // Then
    assertNotNull(result);
    assertEquals(LocalDateTime.of(2024, 2, 10, 10, 0), result);
  }

  @Test
  void stringToRepeatEnum_shouldConvertValidStringToEnum() {
    // Given
    String repeatString = "DAILY";

    // When
    Repeat result = meetingMapperUtils.stringToRepeatEnum(repeatString);

    // Then
    assertNotNull(result);
    assertEquals(Repeat.DAILY, result);
  }

  @Test
  void stringToRepeatEnum_shouldThrowException_whenInvalidEnum() {
    // Given
    String invalidRepeat = "INVALID";

    // When & Then
    assertThrows(InvalidRepeatStringException.class, () -> meetingMapperUtils.stringToRepeatEnum(invalidRepeat));
  }

  @Test
  void changeStringToRepeatEnum_shouldReturnNull_whenInputIsNullOrEmpty() {
    // Given
    String emptyRepeat = "";
    String nullRepeat = null;

    // When & Then
    assertNull(meetingMapperUtils.changeStringToRepeatEnum(emptyRepeat));
    assertNull(meetingMapperUtils.changeStringToRepeatEnum(nullRepeat));
  }

  @Test
  void changeStringToRepeatEnum_shouldConvertValidStringToEnum() {
    // Given
    String repeatString = "WEEKLY";

    // When
    Repeat result = meetingMapperUtils.changeStringToRepeatEnum(repeatString);

    // Then
    assertNotNull(result);
    assertEquals(Repeat.WEEKLY, result);
  }
}
