package xyz.tozymc.configuration.yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import xyz.tozymc.configuration.file.FileConfig;
import xyz.tozymc.configuration.yaml.builder.YamlConfigBuilder;
import xyz.tozymc.configuration.yaml.option.YamlConfigOptions;

/**
 * Type of {@link FileConfig} that is store and handle with {@code .yml} file format.
 *
 * @author TozyMC
 * @see YamlConfigBuilder
 * @since 1.0
 */
public class YamlConfig extends FileConfig {
  private final Yaml yaml;

  /**
   * Constructs new {@link YamlConfig} with configuration file.
   *
   * @param path The configuration file
   */
  public YamlConfig(@NotNull Path path) {
    super(path);
    this.options = new YamlConfigOptions(this);
    this.yaml = new Yaml(getOptions().dumperOptions());
  }

  @Override
  protected Map<String, ?> readToMap() throws IOException {
    return yaml.load(Files.newInputStream(getPath()));
  }

  @Override
  protected String writeToString() {
    return yaml.dumpAs(getValuesWithoutReload(), Tag.MAP, getOptions().flowStyle());
  }

  @Override
  public @NotNull YamlConfigOptions getOptions() {
    return (YamlConfigOptions) super.getOptions();
  }
}
