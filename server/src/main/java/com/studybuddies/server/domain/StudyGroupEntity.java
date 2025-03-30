package com.studybuddies.server.domain;

import com.studybuddies.server.persistance.StudyGroupId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "meeting_id"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyGroupEntity {

    @EmbeddedId
    private StudyGroupId id;

    @MapsId("userId")
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @MapsId("meetingId")
    @OneToOne
    @JoinColumn(name = "meeting_id", nullable = false)
    private MeetingEntity meeting;
}