package com.studybuddies.server.web.dto;

import jakarta.validation.constraints.NotBlank;

public class MeetingResponse {
    @NotBlank
    public String id;
    public String superId;
    @NotBlank
    public String title;
    public String description;
    @NotBlank
    public String dateFrom;
    @NotBlank
    public String dateUntil;
    public String repeatable;
    public String place;
    public String creator;
}