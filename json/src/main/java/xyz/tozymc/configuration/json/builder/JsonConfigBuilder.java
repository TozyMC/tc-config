package xyz.tozymc.configuration.json.builder;

import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;
import xyz.tozymc.configuration.file.builder.AbstractFileConfigBuilder;
import xyz.tozymc.configuration.json.JsonConfig;
import xyz.tozymc.configuration.json.option.JsonConfigOptions;

/**
 * Builder class to create a new {@link JsonConfig}.
 *
 * @author TozyMC
 * @see JsonConfig
 * @see JsonConfigOptions
 * @since 1.0
 */
public class JsonConfigBuilder extends AbstractFileConfigBuilder<JsonConfig> {
  private boolean prettyPrint;

  /**
   * Constructs new file configuration builder with config {@link Path}.
   *
   * @param configPath The configuration file.
   */
  public JsonConfigBuilder(@NotNull Path configPath) {
    super(configPath);
  }

  /**
   * Checks the pretty print is on.
   *
   * @return True is pretty print is on.
   */
  public boolean prettyPrint() {
    return prettyPrint;
  }

  /**
   * Sets the pretty print options.
   *
   * @param prettyPrint New value.
   * @return This builder, for chaining.
   */
  public @NotNull JsonConfigBuilder prettyPrint(boolean prettyPrint) {
    this.prettyPrint = prettyPrint;
    return this;
  }

  @Override
  protected JsonConfig buildConfig() {
    return new JsonConfig(configPath()).getOptions().prettyPrint(prettyPrint).config();
  }
}
