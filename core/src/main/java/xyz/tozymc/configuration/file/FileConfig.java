package xyz.tozymc.configuration.file;

import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;
import xyz.tozymc.configuration.file.option.FileConfigOptions;
import xyz.tozymc.configuration.memory.MemoryConfig;

/**
 * @author TozyMC
 * @since 1.0
 */
public class FileConfig extends MemoryConfig {
  private final Path path;

  protected FileConfig(@NotNull Path path) {
    super();
    this.path = path;
    this.options = new FileConfigOptions(this);
  }

  @Override
  public @NotNull FileConfigOptions getOptions() {
    return (FileConfigOptions) options;
  }

  /**
   * Gets the configuration file.
   *
   * @return The configuration file.
   */
  public @NotNull Path getPath() {
    return path;
  }
}
