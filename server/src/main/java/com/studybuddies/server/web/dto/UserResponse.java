package com.studybuddies.server.web.dto;

import com.studybuddies.server.web.dto.interfaces.Responses;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserResponse implements Responses {
  String uuid;
  String username;
}
