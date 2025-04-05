package com.studybuddies.server.services;

import com.studybuddies.server.services.exceptions.MergeFailedException;
import java.lang.reflect.Field;
import org.springframework.stereotype.Service;

@Service
public class MergingService {

  private MergingService() {
  }

  public static void mergeObjects(Object source, Object target) {
    if (source == null || target == null) {
      throw new IllegalArgumentException("Source and target objects must not be null");
    }

    Class<?> sourceClass = source.getClass();
    Class<?> targetClass = target.getClass();

    if (!sourceClass.equals(targetClass)) {
      throw new IllegalArgumentException("Source and target objects must be of the same type");
    }

    Field[] fields = sourceClass.getDeclaredFields();

    for (Field field : fields) {
      field.setAccessible(true);
      try {
        Object sourceValue = field.get(source);
        if (sourceValue != null) {
          field.set(target, sourceValue);
        }
      } catch (IllegalAccessException e) {
        throw new MergeFailedException("");
      }
    }
  }
}
