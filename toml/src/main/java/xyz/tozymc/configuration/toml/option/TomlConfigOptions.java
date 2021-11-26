package xyz.tozymc.configuration.toml.option;

import org.jetbrains.annotations.NotNull;
import xyz.tozymc.configuration.file.FileConfig;
import xyz.tozymc.configuration.file.option.FileConfigOptions;
import xyz.tozymc.configuration.toml.TomlConfig;

/**
 * Various options for {@link TomlConfig}.
 *
 * @author TozyMC
 * @since 1.0
 */
public class TomlConfigOptions extends FileConfigOptions {
  private int indent = 0;

  /**
   * Creates new {@link TomlConfigOptions} for {@link TomlConfig}.
   *
   * @param config Configuration that owned this options.
   */
  public TomlConfigOptions(@NotNull FileConfig config) {
    super(config);
  }

  /**
   * Gets the indent of between parent and child element.
   *
   * @return The indent of between parent and child element.
   */
  public int indent() {
    return indent;
  }

  /**
   * Sets the indent of between parent and child element.
   *
   * @param indent indent of between parent and child element.
   * @return This object, for chaining.
   */
  public TomlConfigOptions indent(int indent) {
    this.indent = indent;
    return this;
  }

  @Override
  public @NotNull TomlConfig config() {
    return (TomlConfig) super.config();
  }
}
