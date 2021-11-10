package xyz.tozymc.configuration.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnusedReturnValue")
public final class Validators {
  private Validators() {}

  @Contract(value = "null, _ -> fail; !null, _ -> param1", pure = true)
  public static <T> @NotNull T notNullArgs(T obj, String errMessage) {
    if (obj == null) {
      throw new IllegalArgumentException(errMessage);
    }
    return obj;
  }

  @Contract("null, _, _ -> fail; !null, _, _ -> param1")
  public static <T> @NotNull T notNullArgs(T obj, String errMessage, Object... errMessageArgs) {
    if (obj == null) {
      throw new IllegalArgumentException(String.format(errMessage, errMessageArgs));
    }
    return obj;
  }

  @Contract(value = "null, _ -> fail; !null, _ -> param1", pure = true)
  public static <T> @NotNull T notNull(T obj, String errMessage) {
    if (obj == null) {
      throw new NullPointerException(errMessage);
    }
    return obj;
  }
}
