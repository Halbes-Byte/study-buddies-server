package com.studybuddies.server.web.mapper;

import com.studybuddies.server.web.dto.module.ModuleResponse;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class UserMapperUtils {

  @Named("stringListToRespList")
  public List<ModuleResponse> stringListToRespList(List<String> stringList) {
    List<ModuleResponse> respList = new ArrayList<>();
    for (String s : stringList) {
      respList.add(new ModuleResponse(s));
    }
    return respList;
  }
}
