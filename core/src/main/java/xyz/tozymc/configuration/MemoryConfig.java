package xyz.tozymc.configuration;

import org.jetbrains.annotations.NotNull;
import xyz.tozymc.configuration.option.TcConfigOptions;
import xyz.tozymc.configuration.util.Validators;

/**
 * The type of {@link TcConfig} that is stored in memory.
 *
 * @author TozyMC
 * @since 1.0
 */
public abstract class MemoryConfig extends MemoryConfigSection implements TcConfig {
  protected TcConfigOptions options;

  protected MemoryConfig() {
    super();
  }

  @Override
  public @NotNull TcConfigOptions getOptions() {
    return Validators.notNull(options, "Options is not initialized");
  }
}
