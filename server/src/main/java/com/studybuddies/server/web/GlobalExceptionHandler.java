package com.studybuddies.server.web;

import com.studybuddies.server.services.exceptions.MeetingNotFoundException;
import com.studybuddies.server.web.mapper.exceptions.DateFormatException;
import com.studybuddies.server.web.mapper.exceptions.EndDateAfterStartDateException;
import com.studybuddies.server.web.mapper.exceptions.InvalidRepeatStringException;
import com.studybuddies.server.web.mapper.exceptions.TimeFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(DateFormatException.class)
  protected ResponseEntity<?> handleDateFormatException() {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong date format. Please use dd-MM-yyyy:HH:mm");
  }

  @ExceptionHandler(TimeFormatException.class)
  protected ResponseEntity<?> handleTimeFormatException() {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Minutes must be divisible by 15");
  }

  @ExceptionHandler(InvalidRepeatStringException.class)
  protected ResponseEntity<?> handleInvalidRepeatStringException() {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not allowed. (daily, weekly, monthly, never)");
  }

  @ExceptionHandler(EndDateAfterStartDateException.class)
  protected ResponseEntity<?> handleEndDateAfterStartDateException() {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start date must be before end date");
  }

  @ExceptionHandler(MeetingNotFoundException.class)
  protected ResponseEntity<?> handleMeetingNotFoundException() {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Meeting could not be found");
  }
}
