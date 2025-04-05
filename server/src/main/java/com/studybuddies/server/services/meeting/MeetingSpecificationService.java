package com.studybuddies.server.services.meeting;

import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.persistance.MeetingRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingSpecificationService {

  final MeetingRepository meetingRepository;

  public static Specification<MeetingEntity> isThisWeek() {
    return (root, query, builder) -> {
      LocalDate today = LocalDate.now();
      LocalDate endDate = today.plusDays(7);

      return builder.and(
          builder.lessThan(root.get("dateFrom"), endDate),
          builder.greaterThan(root.get("dateFrom"), today)
      );
    };
  }
}
