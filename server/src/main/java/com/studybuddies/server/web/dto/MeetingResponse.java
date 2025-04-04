package com.studybuddies.server.web.dto;

import com.studybuddies.server.web.dto.interfaces.Responses;
import jakarta.validation.constraints.NotBlank;

public class MeetingResponse implements Responses {
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