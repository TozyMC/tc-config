package xyz.tozymc.configuration;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import xyz.tozymc.configuration.exception.TcConfigSerializationException;
import xyz.tozymc.configuration.serialization.TcConfigSerializations;
import xyz.tozymc.configuration.util.NumberConversions;

/**
 * Represents configuration section data storage.
 *
 * @author TozyMC
 * @see TcConfigSection
 * @since 1.0
 */
public interface DataStorage {
  /**
   * Checks if this storage contains this path and the value is instance of the type.
   *
   * @param path Path to value.
   * @param type The class type of value.
   * @param <T>  Type of value
   * @return True if this storage contains this path and the value is instance of the type.
   */
  default <T> boolean contains(@NotNull String path, @NotNull Class<T> type) {
    return type.isInstance(get(path));
  }

  /**
   * Checks if this storage contains this value path.
   *
   * @param path Path to value.
   * @return True if this storage contains this path.
   */
  default boolean contains(@NotNull String path) {
    return get(path) != null;
  }

  /**
   * Gets the value with given path.
   *
   * <p>If the requested is instance of {@link TcConfigSection}, the requested value will be
   * deserialized to new object.
   *
   * <p>If the requested value isn't instance of the type, this method will return {@code null}.
   *
   * @param path Path to value.
   * @param type The class type of value.
   * @param <T>  Type of value.
   * @return The requested object.
   * @throws TcConfigSerializationException Thrown when no serializer represented the type.
   * @see TcConfigSerializations#deserializeObject(Class, Map)
   */
  <T> @Nullable T get(@NotNull String path, @NotNull Class<T> type)
      throws TcConfigSerializationException;

  /**
   * Gets the value with given path, returns the default value if requested value is null.
   *
   * <p>If the requested is instance of {@link TcConfigSection}, the requested value will be
   * deserialized to new object.
   *
   * @param path Path to value.
   * @param type The class type of value.
   * @param def  The default value.
   * @param <T>  Type of value.
   * @return The requested object.
   * @throws TcConfigSerializationException Thrown when no serializer represented the type.
   * @see TcConfigSerializations#deserializeObject(Class, Map)
   */
  <T> @Nullable T getOrDefault(@NotNull String path, @NotNull Class<T> type, @Nullable T def)
      throws TcConfigSerializationException;

  /**
   * Gets the object with given path.
   *
   * @param path Path to object.
   * @return The requested object.
   */
  @Nullable Object get(@NotNull String path);

  /**
   * Gets the value with given path, returns the default value if requested value is null.
   *
   * @param path Path to value.
   * @param def  The default value.
   * @return The requested object.
   */
  @Nullable Object getOrDefault(@NotNull String path, @Nullable Object def);

  /**
   * Finds the object with given path.
   *
   * <p>If the requested value isn't instance of the type, this method will return {@link
   * Optional#empty()}.
   *
   * @param path Value path to find.
   * @param type The class of value.
   * @param <T>  Type of value.
   * @return Optional wrap the requested object.
   */
  default <T> @NotNull Optional<T> find(@NotNull String path, Class<T> type) {
    return Optional.ofNullable(get(path, type));
  }

  /**
   * Finds the object with given path.
   *
   * @param path Value path to find.
   * @return Optional wrap the requested object.
   */
  default @NotNull Optional<?> find(@NotNull String path) {
    return Optional.ofNullable(get(path));
  }

  /**
   * Sets the value to which the path is specified.
   *
   * <p>If {@code value} is null, the path will be removed.
   *
   * @param path  The path where the value will be set.
   * @param value The value to set.
   * @return The previous value.
   */
  @Nullable Object set(@NotNull String path, @Nullable Object value);

  /**
   * Sets the value to which the path is specified, the action is canceled when the path isn't in
   * this storage.
   *
   * @param path  The path where the value will be set.
   * @param value The value to set.
   * @return The previous value.
   */
  @Nullable Object setIfAbsent(@NotNull String path, @Nullable Object value);

  /**
   * Removes the value where path indicated.
   *
   * @param path The path where the value will be removed.
   * @return The previous value.
   * @see #set(String, Object)
   */
  default @Nullable Object remove(@NotNull String path) {
    return set(path, null);
  }

  /**
   * Replaces the value for the given path.
   *
   * @param path     Path to value.
   * @param oldValue The expected value.
   * @param newValue The value to replace.
   * @return True if the value was replaced.
   */
  default boolean replace(@NotNull String path, @NotNull Object oldValue,
      @Nullable Object newValue) {
    var curVal = get(path);
    if (oldValue.equals(curVal) || curVal == null) {
      return false;
    }

    set(path, newValue);
    return true;
  }

  /**
   * Gets the value if existed, else this method attempts to compute its value using the given
   * mapping function and enters it into this storage unless null.
   *
   * @param path      Path to value.
   * @param type      The class type of value.
   * @param mappingFn The mapping function to compute a value.
   * @param <T>       Type of value.
   * @return The current (existing or computed) value, or null if the computed value is null.
   */
  default <T> @Nullable T computeIfAbsent(@NotNull String path, @NotNull Class<T> type,
      @NotNull Function<String, ? extends T> mappingFn) {
    T val;
    if ((val = get(path, type)) != null) {
      T newVal;
      if ((newVal = mappingFn.apply(path)) != null) {
        set(path, newVal);
        return newVal;
      }
    }
    return val;
  }

  /**
   * Attempts to compute a new mapping given the key and its current mapped value, if the value for
   * the given path is present.
   *
   * @param path        Path to value.
   * @param type        The class type of value.
   * @param remappingFn The remapping function to compute a value.
   * @param <T>         Type of value.
   * @return The new value.
   */
  default <T> @Nullable T computeIfPresent(@NotNull String path, @NotNull Class<T> type,
      @NotNull BiFunction<String, ? super T, ? extends T> remappingFn) {
    var oldVal = get(path, type);
    if (oldVal == null) {
      return null;
    }

    var newVal = remappingFn.apply(path, oldVal);
    set(path, newVal);
    return newVal;
  }

  /**
   * Attempts to compute a mapping for the given path and its current mapped value (or null if there
   * is no current mapping).
   *
   * @param path        Path to value.
   * @param type        The class type of value.
   * @param remappingFn The remapping function to compute a value.
   * @param <T>         Type of value.
   * @return The new value.
   */
  default <T> @Nullable T compute(@NotNull String path, @NotNull Class<T> type,
      @NotNull BiFunction<String, ? super T, ? extends T> remappingFn) {
    var oldVal = get(path, type);
    var newVal = remappingFn.apply(path, oldVal);
    if (newVal != null) {
      set(path, newVal);
      return newVal;
    }

    if (oldVal != null) {
      remove(path);
    }
    return null;
  }

  /**
   * Gets the value and recomputes it, then put it into this storage.
   *
   * @param path        Path to object.
   * @param type        The class type of value.
   * @param value       The value to set.
   * @param remappingFn The remapping function to recompute a value if present
   * @param <T>         Type of value
   * @return The new value.
   */
  default <T> @Nullable T merge(@NotNull String path, @NotNull Class<T> type, @Nullable T value,
      @NotNull BiFunction<? super T, ? super T, ? extends T> remappingFn) {
    var oldVal = get(path, type);
    var newVal = oldVal == null ? value : remappingFn.apply(oldVal, value);
    set(path, newVal);
    return newVal;
  }

  /**
   * Gets the shallow set of key that contains in this storage.
   *
   * @return Shallow set of keys contained within this storage.
   * @see #getKeys(boolean)
   */
  default @UnmodifiableView @NotNull Set<String> getKeys() {
    return getKeys(false);
  }

  /**
   * Gets the set of key that contains in this storage.
   *
   * <p>If {@code deep} is {@code true}, this method will take the keys until the key isn't the key
   * of the storage. Otherwise, this method only takes the 1st layer of this storage.
   *
   * @param deep Whether to take the keys of the child storage.
   * @return Set of keys contained within this storage.
   */
  @UnmodifiableView @NotNull Set<String> getKeys(boolean deep);

  /**
   * Gets the copy map of values.
   *
   * <p>{@link Map} will be representing {@link TcConfigSection}.
   *
   * @return Copy map of values.
   */
  @NotNull Map<String, ?> getValues();

  /* Primitive Data */

  // int

  /**
   * Checks if this storage contains this path and the value type is {@code int}.
   *
   * @param path Path to int.
   * @return True if this storage contains this path and the value type is {@code int}.
   * @see #contains(String, Class)
   */
  default boolean isInt(@NotNull String path) {
    return contains(path, Integer.class);
  }

  /**
   * Gets the int value with given path, returns {@code 0} when value wasn't existed.
   *
   * @param path Path to int.
   * @return The requested int.
   * @see #get(String)
   */
  default int getInt(@NotNull String path) {
    return NumberConversions.toInt(get(path));
  }

  // long

  /**
   * Checks if this storage contains this path and the value type is {@code long}.
   *
   * @param path Path to long.
   * @return True if this storage contains this path and the value type is {@code long}.
   * @see #contains(String, Class)
   */
  default boolean isLong(@NotNull String path) {
    return contains(path, Long.class);
  }

  /**
   * Gets the long value with given path, returns {@code 0L} when value wasn't existed.
   *
   * @param path Path to long.
   * @return The requested long.
   * @see #get(String)
   */
  default long getLong(@NotNull String path) {
    return NumberConversions.toLong(get(path));
  }

  // double

  /**
   * Checks if this storage contains this path and the value type is {@code double}.
   *
   * @param path Path to double.
   * @return True if this storage contains this path and the value type is {@code double}.
   * @see #contains(String, Class)
   */
  default boolean isDouble(@NotNull String path) {
    return contains(path, Double.class);
  }

  /**
   * Gets the double value with given path, returns {@code 0D} when value wasn't existed.
   *
   * @param path Path to double.
   * @return The requested double.
   * @see #get(String)
   */
  default double getDouble(@NotNull String path) {
    return NumberConversions.toDouble(get(path));
  }

  // boolean

  /**
   * Checks if this storage contains this path and the value type is {@code boolean}.
   *
   * @param path Path to double.
   * @return True if this storage contains this path and the value type is {@code boolean}.
   * @see #contains(String, Class)
   */
  default boolean isBoolean(@NotNull String path) {
    return contains(path, Boolean.class);
  }

  /**
   * Gets the boolean value with given path, returns {@code false} when value wasn't existed.
   *
   * @param path Path to boolean.
   * @return The requested boolean.
   * @see #get(String)
   */
  default boolean getBoolean(@NotNull String path) {
    var val = get(path);
    if (val instanceof Boolean) {
      return (boolean) val;
    }
    return val instanceof String && Boolean.parseBoolean(String.valueOf(val));
  }

  /* Objective Data */

  // String

  /**
   * Checks if this storage contains this path and the value type is {@link String}.
   *
   * @param path Path to string.
   * @return True if this storage contains this path and the value type is {@link String}.
   * @see #contains(String, Class)
   */
  default boolean isString(@NotNull String path) {
    return contains(path, String.class);
  }

  /**
   * Gets the {@link String} value with given path.
   *
   * @param path Path to string.
   * @return The requested string.
   * @see #get(String)
   * @see String#valueOf(Object)
   */
  default @NotNull String getString(@NotNull String path) {
    return String.valueOf(get(path));
  }

  // List

  /**
   * Checks if this storage contains this path and the value type is {@link List}.
   *
   * @param path Path to list.
   * @return True if this storage contains this path and the value type is {@link List}.
   * @see #contains(String, Class)
   */
  default boolean isList(@NotNull String path) {
    return contains(path, List.class);
  }

  /**
   * Gets the {@link List} value with given path.
   *
   * @param path Path to list.
   * @return The requested list.
   * @see #get(String)
   */
  default @UnmodifiableView @NotNull List<?> getList(@NotNull String path) {
    var val = get(path);
    if (val instanceof List) {
      return Collections.unmodifiableList((List<?>) val);
    }
    return Collections.emptyList();
  }

  /**
   * Gets the {@code List&#60;String>} value with given path.
   *
   * @param path Path to list.
   * @return The requested list.
   * @see #get(String)
   */
  default @UnmodifiableView @NotNull List<String> getStringList(@NotNull String path) {
    var val = get(path);
    if (val instanceof List) {
      return ((List<?>) val).stream().map(String::valueOf).collect(Collectors.toUnmodifiableList());
    }
    return Collections.emptyList();
  }

  /**
   * Gets the {@code List&#60;Integer>} value with given path.
   *
   * @param path Path to list.
   * @return The requested list.
   * @see #get(String)
   */
  default @UnmodifiableView @NotNull List<Integer> getIntegerList(@NotNull String path) {
    var val = get(path);
    if (val instanceof List) {
      return ((List<?>) val).stream()
          .map(NumberConversions::toInt)
          .collect(Collectors.toUnmodifiableList());
    }
    return Collections.emptyList();
  }

  /**
   * Gets the {@code List&#60;Double>} value with given path.
   *
   * @param path Path to list.
   * @return The requested list.
   * @see #get(String)
   */
  default @UnmodifiableView @NotNull List<Double> getDoubleList(@NotNull String path) {
    var val = get(path);
    if (val instanceof List) {
      return ((List<?>) val).stream()
          .map(NumberConversions::toDouble)
          .collect(Collectors.toUnmodifiableList());
    }
    return Collections.emptyList();
  }

  /**
   * Gets the {@code List&#60;Map&#60;String, ?>>} value with given path.
   *
   * @param path Path to list.
   * @return The requested list.
   * @see #get(String)
   */
  default @UnmodifiableView @NotNull List<Map<String, ?>> getMapList(@NotNull String path) {
    var val = get(path);
    if (val instanceof List) {
      return ((List<?>) val).stream()
          .filter(Map.class::isInstance)
          .map(e -> ((Map<?, ?>) e).entrySet()
              .stream()
              .map(entry -> Map.entry(String.valueOf(entry.getKey()), entry.getValue()))
              .collect(Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue, (a, b) -> b)))
          .collect(Collectors.toUnmodifiableList());
    }
    return Collections.emptyList();
  }
}
