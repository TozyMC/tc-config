package xyz.tozymc.configuration.file.builder;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.tozymc.configuration.builder.AbstractConfigBuilder;
import xyz.tozymc.configuration.file.FileConfig;
import xyz.tozymc.configuration.file.option.ReloadableType;

/**
 * Builder class to create a new file configuration.
 *
 * @param <T> The type of file configuration.
 * @author TozyMC
 * @since 1.0
 */
public abstract class AbstractFileConfigBuilder<T extends FileConfig> extends
    AbstractConfigBuilder<T> {
  private final Path configPath;
  private InputStream defaultStream;
  private ReloadableType reloadableType = ReloadableType.MANUALLY;

  /**
   * Constructs new file configuration builder with config {@link Path}.
   *
   * @param configPath The configuration file.
   */
  protected AbstractFileConfigBuilder(Path configPath) {
    this.configPath = configPath;
  }

  /**
   * Gets the configuration file.
   *
   * @return The configuration file.
   */
  public @NotNull Path configPath() {
    return configPath;
  }

  /**
   * Gets the default config input stream.
   *
   * <p>If {@link #configPath} is not existed, the builder will create new file with the data
   * received in this stream.
   *
   * @return The default config input stream.
   */
  public @Nullable InputStream defaultStream() {
    return defaultStream;
  }

  /**
   * Sets the default config input stream.
   *
   * <p>If {@link #configPath} is not existed, the builder will create new file with the data
   * received in this stream.
   *
   * @param defaultStream The input stream.
   * @return This builder, for chaining.
   */
  public @NotNull AbstractFileConfigBuilder<T> defaultStream(@NotNull InputStream defaultStream) {
    this.defaultStream = defaultStream;
    return this;
  }

  /**
   * Sets the default config input stream by class resource.
   *
   * <p>If {@link #configPath} is not existed, the builder will create new file with the data
   * received in this stream.
   *
   * @param classLoader The class loader contain the resource.
   * @param name        The resource name.
   * @return This builder, for chaining.
   */
  public @NotNull AbstractFileConfigBuilder<T> defaultResource(@NotNull ClassLoader classLoader,
      @NotNull String name) {
    this.defaultStream = classLoader.getResourceAsStream(name);
    return this;
  }

  /**
   * Gets the configuration reload type, default is {@link ReloadableType#MANUALLY}.
   *
   * @return Configuration reload type.
   */
  public @NotNull ReloadableType reloadType() {
    return reloadableType;
  }

  /**
   * Sets the configuration reload type.
   *
   * @param reloadableType Reloadable type
   * @return This builder, for chaining.
   */
  public @NotNull AbstractFileConfigBuilder<T> reloadType(@NotNull ReloadableType reloadableType) {
    this.reloadableType = reloadableType;
    return this;
  }

  public T createConfig() {
    if (Files.notExists(configPath)) {
      try {
        Files.createFile(configPath);
      } catch (IOException e) {
        throw new UncheckedIOException(
            "Error occurs when creating new file " + configPath.getFileName().toString(), e);
      }
      if (defaultStream != null) {
        try (var out = Files.newOutputStream(configPath)) {
          defaultStream.transferTo(out);
        } catch (IOException e) {
          throw new UncheckedIOException("Error occurs when transfer default stream to file", e);
        }
      }
    }

    return buildConfig();
  }

  protected abstract T buildConfig();
}