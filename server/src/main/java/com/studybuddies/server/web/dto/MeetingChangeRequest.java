package com.studybuddies.server.web.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MeetingChangeRequest {
    public String title;
    public String description;
    public String date_from;
    public String date_until;
    public String repeatable;
    public String place;
    public String creator;
}
