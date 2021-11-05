package xyz.tozymc.configuration.util;

/**
 * Utilities class to cast object to number if possible.
 *
 * @author TozyMC
 * @since 1.0
 */
public final class NumberConversions {
  private NumberConversions() {}

  /**
   * Converts object to {@code int}, default {@code 0}.
   *
   * @param val The value to convert.
   * @return The converted int.
   */
  public static int toInt(Object val) {
    if (val instanceof Number) {
      return ((Number) val).intValue();
    }
    if (val instanceof String) {
      try {
        return Integer.parseInt(String.valueOf(val));
      } catch (NumberFormatException ignored) {
      }
    }
    return 0;
  }

  /**
   * Converts object to {@code long}, default {@code 0L}.
   *
   * @param val The value to convert.
   * @return The converted long.
   */
  public static long toLong(Object val) {
    if (val instanceof Number) {
      return ((Number) val).longValue();
    }
    if (val instanceof String) {
      try {
        return Long.parseLong(String.valueOf(val));
      } catch (NumberFormatException ignored) {
      }
    }
    return 0L;
  }

  /**
   * Converts object to {@code double}, default {@code 0D}.
   *
   * @param val The value to convert.
   * @return The converted double.
   */
  public static double toDouble(Object val) {
    if (val instanceof Number) {
      return ((Number) val).doubleValue();
    }
    if (val instanceof String) {
      try {
        return Double.parseDouble(String.valueOf(val));
      } catch (NumberFormatException ignored) {
      }
    }
    return 0D;
  }
}
