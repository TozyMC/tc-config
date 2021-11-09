package xyz.tozymc.configuration.serialization;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import xyz.tozymc.configuration.serialization.annotation.SerializeAs;

final class ReflectionCache {
  private final MethodHandles.Lookup lookup = MethodHandles.lookup();
  private final Map<Class<?>, List<AnnotatedField>> annotatedFields = new HashMap<>();
  private final Map<Class<?>, AnnotatedConstructor> annotatedConstructors = new HashMap<>();

  ReflectionCache() {}

  void clear() {
    annotatedFields.clear();
    annotatedConstructors.clear();
  }

  List<AnnotatedField> getAnnotatedFields(Class<?> clazz) {
    if (annotatedFields.containsKey(clazz)) {
      return annotatedFields.get(clazz);
    }

    var annotatedFields = Arrays.stream(clazz.getDeclaredFields())
        .filter(field -> field.isAnnotationPresent(SerializeAs.class))
        .map(AnnotatedField::new)
        .collect(Collectors.toUnmodifiableList());
    this.annotatedFields.put(clazz, annotatedFields);
    return annotatedFields;
  }

  AnnotatedConstructor getAnnotatedConstructor(Class<?> clazz) {
    if (annotatedConstructors.containsKey(clazz)) {
      return annotatedConstructors.get(clazz);
    }

    var annotatedConstructor = findParametersConstructor(clazz);
    if (annotatedConstructor == null) {
      annotatedConstructor = findEmptyConstructor(clazz);
    }

    annotatedConstructors.put(clazz, annotatedConstructor);
    return annotatedConstructor;
  }

  private AnnotatedConstructor findParametersConstructor(Class<?> clazz) {
    List<Class<?>> paramTypes = getAnnotatedFields(clazz).stream()
        .map(f -> f.varHandle.varType())
        .collect(Collectors.toUnmodifiableList());
    try {
      var constructor = lookup.findConstructor(clazz,
          MethodType.methodType(void.class, paramTypes));
      if (constructor != null) {
        return new AnnotatedConstructor(constructor, true);
      }
    } catch (ReflectiveOperationException ignored) {
    }
    return null;
  }

  private AnnotatedConstructor findEmptyConstructor(Class<?> clazz) {
    try {
      var constructor = lookup.findConstructor(clazz, MethodType.methodType(void.class));
      return new AnnotatedConstructor(constructor, false);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException("Type must have an empty constructor to deserialize", e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Error when accessing to empty constructor", e);
    }
  }

  class AnnotatedConstructor {
    private final MethodHandle constructor;
    private final boolean hasParams;

    AnnotatedConstructor(MethodHandle constructor, boolean hasParams) {
      this.constructor = constructor;
      this.hasParams = hasParams;
    }

    Object newInstance(Map<String, ?> params) {
      return hasParams
          ? newInstance0(params.values())
          : setObjectFields(newInstance0(Collections.emptyList()), params);
    }

    private Object newInstance0(Collection<?> values) {
      try {
        return constructor.invokeWithArguments(List.copyOf(values));
      } catch (Throwable e) {
        throw new RuntimeException("Error when invoking new instance", e);
      }
    }

    private Object setObjectFields(Object object, Map<String, ?> map) {
      var annotatedFields = getAnnotatedFields(object.getClass());
      annotatedFields.forEach(f -> f.set(object, map.get(f.key())));
      return object;
    }
  }

  class AnnotatedField {
    private final VarHandle varHandle;
    private final String key;

    private AnnotatedField(Field field) {
      try {
        this.varHandle = lookup.unreflectVarHandle(field);
      } catch (IllegalAccessException e) {
        throw new RuntimeException("Error when converting Field to VarHandle", e);
      }
      this.key = field.getAnnotation(SerializeAs.class).value();
    }

    String key() {
      return key;
    }

    Object value(Object instance) {
      return varHandle.get(instance);
    }

    void set(Object instance, Object value) {
      varHandle.set(instance, value);
    }
  }
}
