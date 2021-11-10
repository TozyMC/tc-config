package xyz.tozymc.configuration.file.option;

import org.jetbrains.annotations.NotNull;
import xyz.tozymc.configuration.file.FileConfig;
import xyz.tozymc.configuration.option.TcConfigOptions;

/**
 * Various options for {@link FileConfig}.
 *
 * @author TozyMC
 * @since 1.0
 */
public class FileConfigOptions extends TcConfigOptions {
  private ReloadableType reloadableType = ReloadableType.MANUALLY;

  /**
   * Creates new {@link FileConfigOptions} for {@link FileConfig}.
   *
   * @param config Configuration that owned this options.
   */
  public FileConfigOptions(@NotNull FileConfig config) {super(config);}

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
   * @return This object, for chaining.
   */
  public @NotNull FileConfigOptions reloadType(@NotNull ReloadableType reloadableType) {
    this.reloadableType = reloadableType;
    return this;
  }

  @Override
  public @NotNull FileConfig config() {
    return (FileConfig) super.config();
  }
}
