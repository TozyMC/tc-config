package xyz.tozymc.configuration.option;

import xyz.tozymc.configuration.TcConfig;
import xyz.tozymc.configuration.TcConfigSection;

/**
 * Various options for {@link TcConfig}.
 *
 * @author TozyMC
 * @since 1.0
 */
public abstract class TcConfigOptions {
  public static final char DEFAULT_PATH_SEPARATOR = '.';

  private char pathSeparator = DEFAULT_PATH_SEPARATOR;

  protected TcConfigOptions() {}

  /**
   * Gets the char that will be used to separate in {@link TcConfigSection}, default is "{@value
   * #DEFAULT_PATH_SEPARATOR}".
   *
   * @return Path separator.
   */
  public char pathSeparator() {
    return pathSeparator;
  }

  /**
   * Sets the char that will be used to separate in {@link TcConfigSection}.
   *
   * @param separator Path separator.
   * @return This object, for chaining.
   */
  public TcConfigOptions pathSeparator(char separator) {
    this.pathSeparator = separator;
    return this;
  }
}
