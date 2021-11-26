package xyz.tozymc.configuration.toml.builder;

import java.nio.file.Path;
import xyz.tozymc.configuration.file.builder.AbstractFileConfigBuilder;
import xyz.tozymc.configuration.toml.TomlConfig;
import xyz.tozymc.configuration.toml.option.TomlConfigOptions;

/**
 * Builder class to create a new {@link TomlConfig}.
 *
 * @author TozyMC
 * @see TomlConfig
 * @see TomlConfigOptions
 * @since 1.0
 */
public class TomlConfigBuilder extends AbstractFileConfigBuilder<TomlConfig> {
  private int indent;

  /**
   * Constructs new file configuration builder with config {@link Path}.
   *
   * @param configPath The configuration file.
   */
  public TomlConfigBuilder(Path configPath) {
    super(configPath);
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
   * @return This builder, for chaining.
   */
  public TomlConfigBuilder indent(int indent) {
    this.indent = indent;
    return this;
  }

  @Override
  protected TomlConfig buildConfig() {
    return new TomlConfig(configPath()).getOptions().indent(indent).config();
  }
}
