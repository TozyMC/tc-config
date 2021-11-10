package xyz.tozymc.configuration.option;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import xyz.tozymc.configuration.TcConfig;
import xyz.tozymc.configuration.TcConfigSection;

/**
 * Various options for {@link TcConfig}.
 *
 * @author TozyMC
 * @since 1.0
 */
public abstract class TcConfigOptions {
  /**
   * Defaults path separator use in configuration.
   */
  public static final char DEFAULT_PATH_SEPARATOR = '.';

  private final TcConfig config;
  private char pathSeparator = DEFAULT_PATH_SEPARATOR;

  /**
   * Creates new {@link TcConfigOptions} for {@link TcConfig}.
   *
   * @param config Configuration that owned this options.
   */
  @Contract(pure = true)
  protected TcConfigOptions(@NotNull TcConfig config) {this.config = config;}

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
  public @NotNull TcConfigOptions pathSeparator(char separator) {
    this.pathSeparator = separator;
    return this;
  }

  /**
   * Returns the {@link TcConfig} that owned this options.
   *
   * @return Configuration that owned this options.
   */
  public @NotNull TcConfig config() {
    return config;
  }
}
