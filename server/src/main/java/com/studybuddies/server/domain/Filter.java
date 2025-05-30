package com.studybuddies.server.domain;

import java.util.HashMap;
import lombok.Getter;

@Getter
public class Filter {
  HashMap<String, String> filters;

  private Filter() {}

  public static Filter of(Filter filter, String key, String value) {
    if(filter == null) {
      filter = new Filter();
      filter.filters = new HashMap<>();
    }
    filter.filters.putIfAbsent(key, value);

    return filter;
  }
}
