package xyz.tozymc.configuration;

import org.jetbrains.annotations.NotNull;
import xyz.tozymc.configuration.option.TcConfigOptions;

/**
 * The type of {@link TcConfig} that is stored in memory.
 *
 * @author TozyMC
 * @since 1.0
 */
public abstract class MemoryConfig extends MemoryConfigSection implements TcConfig {
  protected final TcConfigOptions options;

  protected MemoryConfig(@NotNull TcConfigOptions options) {
    super();
    this.options = options;
  }

  @Override
  public @NotNull TcConfigOptions getOptions() {
    return options;
  }
}
