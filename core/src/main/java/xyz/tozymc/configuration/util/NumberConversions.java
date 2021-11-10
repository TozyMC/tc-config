package xyz.tozymc.configuration.util;

public final class NumberConversions {
  private NumberConversions() {}

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
