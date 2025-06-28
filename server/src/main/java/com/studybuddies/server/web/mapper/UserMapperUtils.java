package com.studybuddies.server.web.mapper;

import com.studybuddies.server.domain.UserModule;
import com.studybuddies.server.web.dto.module.ModuleResponse;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class UserMapperUtils {
  @Named("userModulesToString")
  public List<ModuleResponse> userModulesToJson(List<UserModule> modules) {
    if (modules == null) return new ArrayList<>();

    return modules.stream()
        .map(module -> new ModuleResponse(
            module.getName(),
            module.getExamDate(),
            module.getExamTime(),
            module.getExamLoc(),
            module.getChapter()
        ))
        .toList();
  }

}
