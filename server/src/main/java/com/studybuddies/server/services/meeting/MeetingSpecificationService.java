package com.studybuddies.server.services.meeting;

import com.studybuddies.server.domain.MeetingEntity;
import com.studybuddies.server.domain.StudyGroupEntity;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingSpecificationService {


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

  private static Specification<MeetingEntity> isParticipant(UUID userId) {
    return (root, query, cb) -> {
      Subquery<UUID> subquery = query.subquery(UUID.class);
      Root<StudyGroupEntity> studyGroupRoot = subquery.from(StudyGroupEntity.class);
      subquery.select(studyGroupRoot.get("meeting").get("id"))
          .where(cb.equal(studyGroupRoot.get("user").get("id"), userId));

      return cb.or(cb.in(root.get("id")).value(subquery), cb.equal(root.get("creator").get("id"), userId));
    };
  }

  private static Specification<MeetingEntity> isNotParticipant(UUID userId) {
    return (root, query, cb) -> {
      Subquery<UUID> subquery = query.subquery(UUID.class);
      Root<StudyGroupEntity> sg = subquery.from(StudyGroupEntity.class);

      subquery.select(sg.get("meeting").get("id"))
          .where(cb.equal(sg.get("user").get("id"), userId));

      return cb.and(
          cb.not(root.get("id").in(subquery)),
          cb.notEqual(root.get("creator").get("id"), userId)
      );
    };
  }

  private static Specification<MeetingEntity> isRelevant(String userModules) {
    return (r, q, b) -> b.equal(r.get("module"), userModules);
  }

  public static Specification<MeetingEntity> startsAfter(LocalDateTime start) {
    return (r, q, b) ->
        b.greaterThanOrEqualTo(r.get("dateFrom"), start);
  }

  public static Specification<MeetingEntity> endsBefore(LocalDateTime end) {
    return (r, q, b) ->
        b.lessThanOrEqualTo(r.get("dateUntil"), end);
  }

  public static Specification<MeetingEntity> getSpecRelevantMeetings(
      UUID clientId,
      String modules
  ) {
    return Specification
        .where(isNotParticipant(clientId))
        .and(isRelevant(modules));
  }

  public static Specification<MeetingEntity> getSpecParticipant(UUID userId) {
    return isParticipant(userId);
  }
}
