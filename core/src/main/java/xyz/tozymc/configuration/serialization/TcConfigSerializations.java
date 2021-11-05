package xyz.tozymc.configuration.serialization;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.tozymc.configuration.exception.TcConfigSerializationException;
import xyz.tozymc.configuration.serialization.ReflectionCache.AnnotatedField;
import xyz.tozymc.configuration.serialization.annotation.AutoSerialization;

/**
 * Backend for object serialization.
 *
 * @author TozyMC
 * @since 1.0
 */
public final class TcConfigSerializations {
  private static final Map<Class<?>, TcConfigSerializer<?>> registeredSerializers = new HashMap<>();

  private static final ReflectionCache reflectionCache = new ReflectionCache();

  private TcConfigSerializations() {}

  /**
   * Clears the reflection cache.
   *
   * <p>{@link ReflectionCache} cache constructor, and field need for serialization.
   */
  public static synchronized void clearCache() {
    reflectionCache.clear();
  }

  /**
   * Registers the serializer to the backend.
   *
   * @param serializer Serializer to register.
   * @throws IllegalStateException Thrown when serializer is registered.
   */
  public static synchronized void registerSerializer(@NotNull TcConfigSerializer<?> serializer) {
    if (registeredSerializers.containsValue(serializer)) {
      throw new IllegalStateException(
          "Serializer for " + serializer.getClass().getName() + " is registered");
    }

    Class<?> clazz;
    try {
      clazz = getSerializableObjectType(serializer);
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException("Serializable object type not found", e);
    }
    registeredSerializers.put(clazz, serializer);
  }

  /**
   * Registers the serializers to the backend.
   *
   * @param serializers Serializers to register.
   * @throws IllegalStateException Thrown when serializer is registered.
   */
  public static synchronized void registerSerializers(
      TcConfigSerializer<?> @NotNull ... serializers) {
    Arrays.stream(serializers).forEach(TcConfigSerializations::registerSerializer);
  }

  /**
   * Unregisters the serializer to the backend.
   *
   * @param serializer Serializer to unregister.
   * @throws IllegalStateException Thrown when serializer isn't registered.
   */
  public static synchronized void unregisterSerializer(@NotNull TcConfigSerializer<?> serializer) {
    if (!registeredSerializers.containsValue(serializer)) {
      throw new IllegalStateException(
          "Serializer " + serializer.getClass().getName() + " is not registered");
    }
    //noinspection OptionalGetWithoutIsPresent
    var clazz = registeredSerializers.entrySet()
        .stream()
        .filter(entry -> Objects.equals(entry.getValue(), serializer))
        .findFirst()
        .map(Entry::getKey)
        .get();
    registeredSerializers.remove(clazz);
  }

  /**
   * Unregisters the serializers to the backend.
   *
   * @param serializers Serializers to unregister.
   * @throws IllegalStateException Thrown when serializer isn't registered.
   */
  public static synchronized void unregisterSerializers(
      TcConfigSerializer<?> @NotNull ... serializers) {
    Arrays.stream(serializers).forEach(TcConfigSerializations::unregisterSerializer);
  }

  private static @NotNull Class<?> getSerializableObjectType(
      @NotNull TcConfigSerializer<?> serializer) throws ClassNotFoundException {
    var typeName = serializer.getClass().getGenericInterfaces()[0].getTypeName();
    return Class.forName(typeName.substring(typeName.indexOf('<') + 1, typeName.indexOf('>')));
  }

  /**
   * Creates map represented the object.
   *
   * @param type   The class type of serializable object.
   * @param object Object to serialize
   * @param <T>    Type of serializable object.
   * @return Map represented the object.
   * @throws TcConfigSerializationException Thrown when no serializer represented the type.
   */
  public static <T> @NotNull Map<String, ?> serializeObject(@NotNull Class<T> type, T object) {
    var serialized = serializeObjectBySerializer(type, object);
    if (serialized != null) {
      return serialized;
    }
    if (!type.isAnnotationPresent(AutoSerialization.class)) {
      throw new TcConfigSerializationException(
          "Couldn't find any serializer for " + type.getName());
    }
    return serializeAnnotatedObject(type, object);
  }

  private static <T> @Nullable Map<String, ?> serializeObjectBySerializer(@NotNull Class<T> type,
      T object) {
    TcConfigSerializer<T> serializer;
    //noinspection unchecked
    if ((serializer = (TcConfigSerializer<T>) registeredSerializers.get(type)) == null) {
      return null;
    }

    return serializer.serialize(object);
  }

  private static @NotNull Map<String, ?> serializeAnnotatedObject(@NotNull Class<?> type,
      Object object) {
    return reflectionCache.getAnnotatedFields(type)
        .stream()
        .collect(Collectors.toMap(AnnotatedField::key, f -> f.value(object), (a, b) -> a,
            LinkedHashMap::new));
  }

  /**
   * Creates new object from represented map.
   *
   * @param type       The class type of deserializable object.
   * @param serialized Map represented the object.
   * @param <T>        Type of serializable object.
   * @return New object created from represented map.
   * @throws TcConfigSerializationException Thrown when no serializer represented the type.
   */
  public static <T> @NotNull T deserializeObject(@NotNull Class<T> type,
      Map<String, ?> serialized) {
    var deserialized = deserializeObjectBySerializer(type, serialized);
    if (deserialized != null) {
      return deserialized;
    }
    if (!type.isAnnotationPresent(AutoSerialization.class)) {
      throw new TcConfigSerializationException(
          "Couldn't find any serializer for " + type.getName());
    }
    return deserializeAnnotatedObject(type, serialized);
  }

  private static <T> @Nullable T deserializeObjectBySerializer(@NotNull Class<T> type,
      Map<String, ?> serialized) {
    TcConfigSerializer<T> serializer;
    //noinspection unchecked
    if ((serializer = (TcConfigSerializer<T>) registeredSerializers.get(type)) == null) {
      return null;
    }

    return serializer.deserialize(serialized);
  }

  private static <T> T deserializeAnnotatedObject(@NotNull Class<T> type,
      Map<String, ?> serialized) {
    return type.cast(reflectionCache.getAnnotatedConstructor(type).newInstance(serialized));
  }
}
