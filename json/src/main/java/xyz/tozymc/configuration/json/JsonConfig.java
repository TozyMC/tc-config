package xyz.tozymc.configuration.json;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import xyz.tozymc.configuration.file.FileConfig;
import xyz.tozymc.configuration.json.builder.JsonConfigBuilder;
import xyz.tozymc.configuration.json.option.JsonConfigOptions;

/**
 * Type of {@link FileConfig} that is store and handle with {@code .json} file format.
 *
 * @author TozyMC
 * @see JsonConfigBuilder
 * @since 1.0
 */
public class JsonConfig extends FileConfig {
  /**
   * Constructs new {@link JsonConfig} with configuration file.
   *
   * @param path The configuration file
   */
  public JsonConfig(@NotNull Path path) {
    super(path);
    this.options = new JsonConfigOptions(this);
  }

  @Override
  protected Map<String, ?> readToMap() throws IOException {
    //noinspection unchecked
    return getOptions().gson()
        .fromJson(Files.newBufferedReader(getPath(), StandardCharsets.UTF_8), Map.class);
  }

  @Override
  protected String writeToString() {
    return getOptions().gson().toJson(getValuesWithoutReload());
  }

  @Override
  public @NotNull JsonConfigOptions getOptions() {
    return (JsonConfigOptions) super.getOptions();
  }
}
