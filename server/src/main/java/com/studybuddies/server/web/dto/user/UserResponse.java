package com.studybuddies.server.web.dto.user;

import com.studybuddies.server.web.dto.interfaces.Responses;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserResponse implements Responses {

  String uuid;
  String username;
  List<String> modules;
}
