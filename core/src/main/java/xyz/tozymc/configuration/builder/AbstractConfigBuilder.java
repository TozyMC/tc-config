package xyz.tozymc.configuration.builder;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import xyz.tozymc.configuration.TcConfig;
import xyz.tozymc.configuration.TcConfigSection;
import xyz.tozymc.configuration.option.TcConfigOptions;

/**
 * Builder class to create a new configuration.
 *
 * @param <T> The type of configuration.
 */
public abstract class AbstractConfigBuilder<T extends TcConfig> {
  private char pathSeparator;

  /**
   * Constructs new configuration builder.
   */
  @Contract(pure = true)
  protected AbstractConfigBuilder() {}

  /**
   * Gets the char that will be used to separate in {@link TcConfigSection}, default is "{@value
   * TcConfigOptions#DEFAULT_PATH_SEPARATOR}".
   *
   * @return Path separator.
   */
  public char pathSeparator() {
    return pathSeparator;
  }

  /**
   * Sets the char that will be used to separate in {@link TcConfigSection}.
   *
   * @param pathSeparator Path separator.
   * @return This builder, for chaining.
   */
  public @NotNull AbstractConfigBuilder<T> pathSeparator(char pathSeparator) {
    this.pathSeparator = pathSeparator;
    return this;
  }

  /**
   * Creates new configuration with specified parameters.
   *
   * @return The new configuration.
   */
  public abstract T createConfig();
}
