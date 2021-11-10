package xyz.tozymc.configuration.serialization;

import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a serializer to serialize and deserialize object.
 *
 * @param <T> Serializable object type.
 * @author TozyMC
 * @since 1.0
 */
public interface TcConfigSerializer<T> {
  /**
   * Creates a {@link Map} represents specified object.
   *
   * @param object Object to serialize.
   * @return Serialized object.
   */
  @NotNull Map<String, ?> serialize(@NotNull T object);

  /**
   * Creates an object form represented {@link Map}.
   *
   * @param serialized Serialized object to deserialize.
   * @return Deserialized object.
   */
  @NotNull T deserialize(@NotNull Map<String, ?> serialized);
}
