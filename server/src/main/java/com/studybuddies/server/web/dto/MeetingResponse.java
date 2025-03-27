package com.studybuddies.server.web.dto;

import jakarta.validation.constraints.NotBlank;

public class MeetingResponse {
    @NotBlank
    public Long id;
    @NotBlank
    public String title;
    public String description;
    @NotBlank
    public String date_from;
    @NotBlank
    public String date_until;
    public String repeatable;
    public String place;
    public String creator;
}