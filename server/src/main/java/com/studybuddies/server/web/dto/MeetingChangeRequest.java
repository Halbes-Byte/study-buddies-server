package com.studybuddies.server.web.dto;

import jakarta.validation.constraints.NotBlank;

public class MeetingChangeRequest {
    @NotBlank
    public String title;
    public String description;
    public String links;
    @NotBlank
    public String date_from;
    @NotBlank
    public String date_until;
    public String repeatable;
    public String place;
}
