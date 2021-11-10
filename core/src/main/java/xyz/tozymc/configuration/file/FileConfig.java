package xyz.tozymc.configuration.file;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import xyz.tozymc.configuration.TcConfig;
import xyz.tozymc.configuration.TcConfigSection;
import xyz.tozymc.configuration.file.option.FileConfigOptions;
import xyz.tozymc.configuration.file.option.ReloadableType;

/**
 * Type of {@link TcConfigSection} that is store and handle with file.
 *
 * @author TozyMC
 * @since 1.0
 */
public abstract class FileConfig extends FileConfigSection implements TcConfig {
  private final Path path;
  protected FileConfigOptions options;
  private FileTime lastModifiedTime;

  /**
   * Constructs new {@link FileConfigSection} with configuration file.
   *
   * @param path The configuration file
   */
  protected FileConfig(@NotNull Path path) {
    super();
    this.path = path;
    this.options = new FileConfigOptions(this);
    updateLastModifiedTimeIfNeeded();
  }

  /**
   * Loads value to map from file.
   *
   * @return Loaded values map.
   * @throws IOException Thrown when error when reading file.
   */
  protected abstract Map<String, ?> readToMap() throws IOException;

  /**
   * Writes this config to string to save in file.
   *
   * @return New string.
   */
  protected abstract String writeToString();

  /**
   * Reads data from file and loads it to memory.
   */
  public void reload() {
    Map<String, ?> values;
    try {
      values = readToMap();
    } catch (IOException e) {
      throw new UncheckedIOException("Error when loading configuration", e);
    }
    reloadSection(values);
  }

  /**
   * Writes data in memory to string and saves it to file.
   */
  public void save() {
    var data = writeToString();
    try {
      Files.write(path, data.getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new UncheckedIOException("Error when save configuration", e);
    }
    updateLastModifiedTimeIfNeeded();
  }

  private void updateLastModifiedTimeIfNeeded() {
    if (options.reloadType().equals(ReloadableType.INTELLIGENT)) {
      updateLastModifiedTime();
    }
  }

  private FileTime updateLastModifiedTime() {
    var peek = lastModifiedTime;
    try {
      this.lastModifiedTime = Files.getLastModifiedTime(path);
    } catch (IOException e) {
      throw new UncheckedIOException("Error when update last configuration modified time", e);
    }
    return peek;
  }

  private boolean shouldReload() {
    var peek = updateLastModifiedTime();
    return peek.compareTo(lastModifiedTime) != 0;
  }

  protected void reloadIfNeeded() {
    var reloadableType = options.reloadType();
    if (reloadableType.equals(ReloadableType.MANUALLY)) {
      return;
    }
    if (reloadableType.equals(ReloadableType.AUTOMATICALLY)) {
      reload();
      return;
    }
    if (shouldReload()) {
      reload();
    }
  }

  @Override
  public @NotNull FileConfigOptions getOptions() {
    return options;
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
