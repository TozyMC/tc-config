package xyz.tozymc.configuration.toml;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import xyz.tozymc.configuration.file.FileConfig;
import xyz.tozymc.configuration.toml.builder.TomlConfigBuilder;
import xyz.tozymc.configuration.toml.internal.Toml;
import xyz.tozymc.configuration.toml.option.TomlConfigOptions;

/**
 * Type of {@link FileConfig} that is store and handle with {@code .toml} file format.
 *
 * @author TozyMC
 * @see TomlConfigBuilder
 * @since 1.0
 */
public class TomlConfig extends FileConfig {
  /**
   * Constructs new {@link TomlConfig} with configuration file.
   *
   * @param path The configuration file
   */
  public TomlConfig(@NotNull Path path) {
    super(path);
    this.options = new TomlConfigOptions(this);
  }

  @Override
  protected Map<String, ?> readToMap() throws IOException {
    return Toml.fromToml(getPath());
  }

  @Override
  protected String writeToString() {
    return Toml.toToml(getValuesWithoutReload(), getOptions().indent());
  }

  @Override
  public @NotNull TomlConfigOptions getOptions() {
    return (TomlConfigOptions) super.getOptions();
  }
}
